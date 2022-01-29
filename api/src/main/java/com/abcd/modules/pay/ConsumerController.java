package com.abcd.modules.pay;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;

import com.abcd.common.annotation.SystemLog;
import com.abcd.common.dubbo.ChannelUtils;
import com.abcd.common.enums.LogType;
import com.abcd.common.enums.OederRspEnum;
import com.abcd.common.utils.ResultUtil;
import com.abcd.common.vo.Result;
import com.abcd.modules.channel.service.IChannelProductAuthService;
import com.abcd.modules.log.service.OrderFlowService;
import com.abcd.modules.merchant.entity.MerInfo;
import com.abcd.modules.merchant.entity.MerProductAuth;
import com.abcd.modules.merchant.entity.MerSecretKey;
import com.abcd.modules.merchant.service.IMerInfoService;
import com.abcd.modules.merchant.service.IMerSecretKeyService;
import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.order.service.IOrderInfoService;
import com.abcd.modules.pay.common.CountService;
import com.abcd.modules.pay.common.Factor;
import com.abcd.modules.pay.common.IpUtil;
import com.abcd.modules.pay.common.SignUtil;
import com.abcd.modules.pay.entity.PayOrderDto;
import com.abcd.modules.pay.entity.PayOrderRspDto;
import com.abcd.modules.pay.entity.RuiyouNoticeDto;
import com.abcd.modules.pay.entity.SearchMerAccountDto;
import com.abcd.modules.pay.entity.SearchOrderBy30Dto;
import com.abcd.modules.pay.entity.SearchOrderDto;
import com.abcd.modules.pay.entity.SearchOrderRspDto;
import com.abcd.modules.pay.entity.YxyNoticeDto;
import com.abcd.modules.pay.service.PayOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryhy.base.dto.ChannelPayDTO;
import com.ryhy.base.vo.ResultVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 消费测试接口
 * @Author 
 * @CreateTime 2021/04/12 15:09
 */
@Slf4j
@RestController
@Api(description = "测试接口")
@RequestMapping("/api")
@Transactional
public class ConsumerController {
    //服务注册中心地址
    @Value("${dubbo.registry.address}")
    private  String address;
    @Value("${api.key30}")
    private  String key30;
    @Resource
    private IMerInfoService merInfoService;
    @Resource
    private IMerSecretKeyService merSecretKeyService;
    @Resource
    private PayOrderService payOrderService;
    @Resource
    private IOrderInfoService orderInfoService;
    @Resource
    private IChannelProductAuthService channelProductAuthService;
    @Resource
	private CountService countService;
	@Value("${order.notify.delay}")
	private  String notifyMerDelayQueue;
	@Value("${order.notify.queue}")
	private  String notifyMerQueue;
	@Value("${order.search.queue.post}")
	private  String searchOrderQueue;
	@Resource
	private OrderFlowService orderFlowService;
	@Resource
    RabbitTemplate rabbitTemplate;
    //服务版本号
    @Value("${api.version}")
    private  String version;

    @RequestMapping(value = "/payOrder", method = RequestMethod.POST)
    @ApiOperation(value = "充值接口")
    public Result<Object> payOrder(@RequestBody PayOrderDto dto,HttpServletRequest request) {
    	String orderNo = dto.getOrderNo();
    	if(orderNo==null||orderNo.equals("")) {
    		orderFlowService.sendOrderFlow(DateUtil.format(new Date(), "yyyyMMdd"), "订单号为空：{"+JSONUtil.toJsonStr(dto)+"}");
	    	return ResultUtil.error(Integer.valueOf(OederRspEnum.CODE_20004.code),OederRspEnum.CODE_20004.desc);
    	}else {
    		orderFlowService.sendOrderFlow(orderNo, "充值订单入参：{"+JSONUtil.toJsonStr(dto)+"}");
    	}
    	String merchantNo = dto.getMerNo();
    	String ipAddr = IpUtil.getIpAddr(request);
    	dto.setIp(ipAddr);
    	//查询商户是否存在
    	QueryWrapper<MerInfo> queryWrapper=new QueryWrapper<MerInfo>();
    	queryWrapper.lambda().eq(MerInfo::getMerNo, merchantNo);
    	MerInfo merInfo = merInfoService.getOne(queryWrapper);
    	if(merInfo==null) {
    		orderFlowService.sendOrderFlow(orderNo, "商户号不正确：{"+merchantNo+"}");
    		return ResultUtil.error(Integer.valueOf(OederRspEnum.CODE_40001.code),OederRspEnum.CODE_40001.desc);
    	}
    	if(merInfo.getWhiteIp()!=null&&!merInfo.getWhiteIp().equals("")) {
	    	if(!merInfo.getWhiteIp().contains(ipAddr)) {
	    		orderFlowService.sendOrderFlow(orderNo, "白名单IP限制，当前入单IP： "+ipAddr);
	    		return ResultUtil.error(Integer.valueOf(OederRspEnum.CODE_40003.code),OederRspEnum.CODE_40003.desc);
	    	}
    	}
    	QueryWrapper<MerSecretKey> merSecretKeyWrapper=new QueryWrapper<MerSecretKey>();
    	merSecretKeyWrapper.lambda().eq(MerSecretKey::getMerId, merInfo.getId());
    	MerSecretKey merSecretKey = merSecretKeyService.getOne(merSecretKeyWrapper);
    	
    	Boolean verification = SignUtil.verification(dto, merSecretKey.getMd5Key(), dto.getSign());
    	if(!verification) {
    		orderFlowService.sendOrderFlow(orderNo, "验签失败：{"+JSONUtil.toJsonStr(dto)+"}");
    		return ResultUtil.error(Integer.valueOf(OederRspEnum.CODE_90001.code),OederRspEnum.CODE_90001.desc);
    	}
    	PayOrderRspDto orderRspDto = payOrderService.payOrder(dto,merInfo);
    	String sign = SignUtil.sign(orderRspDto, merSecretKey.getMd5Key());
    	orderRspDto.setSign(sign);
    	Result<Object> data = ResultUtil.data(orderRspDto);
    	if(!orderRspDto.getCode().equals("10000")) {
    		String sysOrderNo = orderRspDto.getSysOrderNo();
    		if(sysOrderNo!=null) {
    			QueryWrapper<OrderInfo>orderWrapper=new QueryWrapper<OrderInfo>();
    			orderWrapper.lambda().eq(OrderInfo::getSysOrderNo, sysOrderNo);
    			OrderInfo orderInfo=orderInfoService.getOne(orderWrapper);
    			//下单失败发送通知mq
    			rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
    		}
    	}
    	return data;
    }
    @RequestMapping(value = "/searchOrder", method = RequestMethod.POST)
    @ApiOperation(value = "查询接口")
    public Result<Object> searchOrder(@RequestBody SearchOrderDto dto) {
    	log.info("查询订单入参："+JSONUtil.toJsonStr(dto));
    	String merchantNo = dto.getMerNo();
    	//查询商户是否存在
    	QueryWrapper<MerInfo> queryWrapper=new QueryWrapper<MerInfo>();
    	queryWrapper.lambda().eq(MerInfo::getMerNo, merchantNo);
    	MerInfo merInfo = merInfoService.getOne(queryWrapper);
    	if(merInfo==null) {
    		return ResultUtil.error(Integer.valueOf(OederRspEnum.CODE_40001.code),OederRspEnum.CODE_40001.desc);
    	}
    	QueryWrapper<MerSecretKey> merSecretKeyWrapper=new QueryWrapper<MerSecretKey>();
    	merSecretKeyWrapper.lambda().eq(MerSecretKey::getMerId, merInfo.getId());
    	MerSecretKey merSecretKey = merSecretKeyService.getOne(merSecretKeyWrapper);
    	
    	Boolean verification = SignUtil.verification(dto, merSecretKey.getMd5Key(), dto.getSign());
    	if(!verification) {
    		return ResultUtil.error(Integer.valueOf(OederRspEnum.CODE_90001.code),OederRspEnum.CODE_90001.desc);
    	}
    	SearchOrderRspDto orderRspDto = payOrderService.searchOrder(dto,merInfo);
    	String sign = SignUtil.sign(orderRspDto, merSecretKey.getMd5Key());
    	orderRspDto.setSign(sign);
    	return ResultUtil.data(orderRspDto);
    }
    @RequestMapping(value = "/searchMerAccount", method = RequestMethod.POST)
    @ApiOperation(value = "查询余额接口")
    public Result<Object> searchMerAccount(@RequestBody SearchOrderDto dto) {
    	log.info("查询余额入参："+JSONUtil.toJsonStr(dto));
    	String merchantNo = dto.getMerNo();
    	//查询商户是否存在
    	QueryWrapper<MerInfo> queryWrapper=new QueryWrapper<MerInfo>();
    	queryWrapper.lambda().eq(MerInfo::getMerNo, merchantNo);
    	MerInfo merInfo = merInfoService.getOne(queryWrapper);
    	if(merInfo==null) {
    		return ResultUtil.error(Integer.valueOf(OederRspEnum.CODE_40001.code),OederRspEnum.CODE_40001.desc);
    	}
    	QueryWrapper<MerSecretKey> merSecretKeyWrapper=new QueryWrapper<MerSecretKey>();
    	merSecretKeyWrapper.lambda().eq(MerSecretKey::getMerId, merInfo.getId());
    	MerSecretKey merSecretKey = merSecretKeyService.getOne(merSecretKeyWrapper);
    	
    	Boolean verification = SignUtil.verification(dto, merSecretKey.getMd5Key(), dto.getSign());
    	if(!verification) {
    		return ResultUtil.error(Integer.valueOf(OederRspEnum.CODE_90001.code),OederRspEnum.CODE_90001.desc);
    	}
    	SearchMerAccountDto accountDto = payOrderService.searchMerAccount(dto, merInfo);
    	String sign = SignUtil.sign(accountDto, merSecretKey.getMd5Key());
    	accountDto.setSign(sign);
    	return ResultUtil.data(accountDto);
    }
    
    public static void main(String[] args) {
    	MerInfo merInfo=new MerInfo();
    	if(merInfo.getWhiteIp()!=null&&!merInfo.getWhiteIp().equals("")) {
    		System.out.println("1");
    	}
	}
}