package com.abcd.modules.pay.service.impl;

import cn.hutool.Hutool;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import com.abcd.common.dubbo.ChannelUtils;
import com.abcd.common.utils.MD5Helper;
import com.abcd.modules.channel.entity.ChannelInfo;
import com.abcd.modules.channel.entity.ChannelSettingInfo;
import com.abcd.modules.channel.service.IChannelInfoService;
import com.abcd.modules.channel.service.IChannelSettingInfoService;
import com.abcd.modules.log.service.OrderFlowService;
import com.abcd.modules.order.entity.ChannelOrderInfo;
import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.order.service.IChannelOrderInfoService;
import com.abcd.modules.order.service.IOrderInfoService;
import com.abcd.modules.pay.common.SignDemo;
import com.abcd.modules.pay.common.SignUtil;
import com.abcd.modules.pay.dto.AccountUpdateDto;
import com.abcd.modules.pay.dto.OrderDoubleBookingDto;
import com.abcd.modules.pay.entity.D1NoticeDto;
import com.abcd.modules.pay.entity.D3NoticeDto;
import com.abcd.modules.pay.entity.D4NoticeDto;
import com.abcd.modules.pay.entity.D6NoticeDto;
import com.abcd.modules.pay.entity.RuiyouNoticeDto;
import com.abcd.modules.pay.entity.SearchOrderRspDto;
import com.abcd.modules.pay.service.NoticeService;
import com.abcd.modules.pay.service.PayOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryhy.base.dto.ChannelNoticeDTO;
import com.ryhy.base.dto.ChannelNoticeRspDTO;
import com.ryhy.base.vo.ResultVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {
	@Resource
	private IOrderInfoService orderInfoService;
	@Resource
	private IChannelOrderInfoService channelOrderInfoService;
	@Resource
	private IChannelSettingInfoService channelSettingInfoService;
	@Resource
	private IChannelInfoService channelInfoService;
	@Value("${order.notify.queue}")
	private  String notifyMerQueue;
	@Value("${order.account.queue}")
	private  String updateAccountQueue;
	@Resource
	private PayOrderService payOrderService;
	@Resource
	RabbitTemplate rabbitTemplate;
	@Resource
	private OrderFlowService orderFlowService;
	@Autowired
	private RedisTemplate redisTemplate;
	@Value("${api.maxtime}")
	private Long maxTime;
	private  String address;
	@Value("${api.version}")
    private  String version;
	/**
	 *
	 * @param orderInfo
	 * @param type 操作类型 1新增订单  2订单成功 3订单失败
	 * @param accountType 1修改商户账户 2修改通道账户  3一起修改
	 * @return
	 */
	private boolean updateAccount(OrderInfo orderInfo, int type, int accountType) {
		boolean rs=true;
		//处理资金
		try {
			AccountUpdateDto accountDto=new AccountUpdateDto();
			accountDto.setType(type);
			accountDto.setAccountType(accountType);
			accountDto.setMerId(orderInfo.getMerId());
			accountDto.setSysAmt(orderInfo.getSysActAmt().longValue());
			accountDto.setMerAmt(orderInfo.getMerActAmt().longValue());
			accountDto.setChannelId(orderInfo.getChannelId());
			accountDto.setOrderId(orderInfo.getId());
			rabbitTemplate.convertAndSend(updateAccountQueue, JSONUtil.toJsonStr(accountDto));
		} catch (Exception e) {
			e.printStackTrace();
			rs=false;
		}
		return rs;
	}
	@Override
	public String paramNotice(Map<String, String> resMap, String channelCode,String channelOrderNo,String ipAddr) {
		QueryWrapper<ChannelOrderInfo> channelOrderWrapper=new QueryWrapper<ChannelOrderInfo>();
		channelOrderWrapper.lambda().eq(ChannelOrderInfo::getChannelOrderNo, channelOrderNo);
		channelOrderWrapper.lambda().eq(ChannelOrderInfo::getStatus, 1);
		ChannelOrderInfo channelOrderInfo = channelOrderInfoService.getOne(channelOrderWrapper);
		if(channelOrderInfo==null) {
			orderFlowService.sendOrderFlow(channelOrderNo,"收到通知未查询到通道订单，不做任何处理：");
			return "success";
		}
		QueryWrapper<OrderInfo>orderInfoWrapper=new QueryWrapper<OrderInfo>();
		orderInfoWrapper.lambda().eq(OrderInfo::getSysOrderNo, channelOrderInfo.getSysOrderNo());
		orderInfoWrapper.lambda().eq(OrderInfo::getChannelCode, channelCode);
		orderInfoWrapper.lambda().eq(OrderInfo::getStatus, 1);
		OrderInfo orderInfo=orderInfoService.getOne(orderInfoWrapper);
		//订单不存在不处理
		if(orderInfo==null) {
			orderFlowService.sendOrderFlow(channelOrderNo,"收到通知未查询到主订单，不做任何处理：");
			return "success";
		}
		ChannelInfo channelInfo = channelInfoService.getById(orderInfo.getChannelId());
		String reStr=channelInfo.getReStr();
		if(reStr==null||reStr.equals("1")) {
			reStr="success,fail";
		}
		String[] split = reStr.split(",");
		String success=split[0];
		String fail=split[1];
		if(channelInfo.getWhiteIp()!=null&&!channelInfo.getWhiteIp().equals("")) {
			if(!channelInfo.getWhiteIp().contains(ipAddr)) {
				orderFlowService.sendOrderFlow(channelOrderNo,orderInfo.getChannelCode()+"通道通知白名单限制，当前通知地址："+ipAddr);
				return fail;
			}
		}
		Long channelSettingId = orderInfo.getChannelSettingId();
		ChannelSettingInfo channelSettingInfo = channelSettingInfoService.getById(channelSettingId);
		String md5Key = channelSettingInfo.getMd5Key();
		ChannelNoticeDTO noticeDto=new ChannelNoticeDTO();
		noticeDto.setChannelKey(md5Key);
		noticeDto.setChannelCode(channelInfo.getGroupCode());
		noticeDto.setAddress(address);
		noticeDto.setVersion(version);
		noticeDto.setChannelOrderNo(channelOrderNo);
		noticeDto.setResMap(resMap);
		noticeDto.setResStr(JSONUtil.toJsonStr(resMap));
		ResultVO resultVO = ChannelUtils.invokeNotice(noticeDto);
		ChannelNoticeRspDTO rspDto= (ChannelNoticeRspDTO) resultVO.getData();
		orderFlowService.sendOrderFlow(channelOrderNo,orderInfo.getChannelCode()+"通道通知微服务返回消息"+JSONUtil.toJsonStr(rspDto));
		if(!rspDto.isVerification()) {
//			log.info("验签失败"+channelOrderInfo.getSysOrderNo());
			orderFlowService.sendOrderFlow(channelOrderNo,orderInfo.getChannelCode()+"通道通知验签失败"+channelOrderInfo.getSysOrderNo());
			return fail;
		}
		channelOrderInfo.setSerialNumber(rspDto.getSerialNumber());
		orderInfo.setSerialNumber(rspDto.getSerialNumber());
		if(rspDto.getStatus()==2) {
			channelOrderInfo.setStatus(2);
			channelOrderInfo.setResultDate(new Date());
			boolean saveChannelOrderRs = channelOrderInfoService.updateById(channelOrderInfo);
        	if(!saveChannelOrderRs) {
				return fail;
			}
			orderInfo.setStatus(2);
			orderInfo.setResultTime(new Date());
			orderInfo.setUpdateTime(new Date());
//			orderInfo.setSerialNumber(dto.getMobileOrderId());
			boolean saveOrderRs = orderInfoService.updateById(orderInfo);
        	if(!saveOrderRs) {
        		return fail;
			}
			//处理资金
			//订单成功  修改账户资金
			updateAccount(orderInfo, 2, 3);
			rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
		}else if(rspDto.getStatus()==3) {
			channelOrderInfo.setStatus(3);
			channelOrderInfo.setResultDate(new Date());
			boolean saveChannelOrderRs = channelOrderInfoService.updateById(channelOrderInfo);
        	if(!saveChannelOrderRs) {
				return fail;
			}
			
			
			
			maxTime=orderInfo.getMaxTime();
			Date createTime = orderInfo.getCreateTime();
			long useTime=(new Date().getTime()-createTime.getTime())/1000;
			if(maxTime-useTime>0) {
				//订单失败  回滚账户资金
				updateAccount(orderInfo, 3, 3);
				if(redisTemplate.hasKey(orderInfo.getSysOrderNo())) {
					orderInfo.setUseTime(useTime);
					orderInfoService.updateById(orderInfo);
					OrderDoubleBookingDto orderDto = (OrderDoubleBookingDto) redisTemplate.opsForValue().get(orderInfo.getSysOrderNo());
					if(!orderDto.getChannelIdList().contains(orderInfo.getChannelId())) {
    					orderDto.getChannelIdList().add(orderInfo.getChannelId());
    				}
					orderDto.setOrderInfo(orderInfo);
					redisTemplate.opsForValue().set(orderInfo.getSysOrderNo(), orderDto,3, TimeUnit.MINUTES);
					payOrderService.doubliBookingOrder(orderInfo.getSysOrderNo());
				}else {
					OrderDoubleBookingDto orderDto=new OrderDoubleBookingDto();
					orderInfo.setUseTime(useTime);
					orderInfoService.updateById(orderInfo);
					List<Long> channelIdList =new ArrayList<Long>();
					channelIdList.add(orderInfo.getChannelId());
					orderDto.setChannelIdList(channelIdList);
					orderDto.setOrderInfo(orderInfo);
					redisTemplate.opsForValue().set(orderInfo.getSysOrderNo(), orderDto,3, TimeUnit.MINUTES);
					payOrderService.doubliBookingOrder(orderInfo.getSysOrderNo());
				}
			}else {
				//超时，直接处理为失败并回滚商户资金
				orderInfo.setStatus(3);
				orderInfo.setUpdateTime(new Date());
				orderInfo.setResultTime(new Date());
				boolean saveOrderRs = orderInfoService.updateById(orderInfo);
	        	if(!saveOrderRs) {
	        		return fail;
				}
				//订单失败  回滚账户资金
				updateAccount(orderInfo, 3, 3);
				//通知商户结果
				rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
			}
		}
		return success;
	}
	@Override
	public String streamNotice(String resStr, String channelCode,String channelOrderNo,String ipAddr) {
		QueryWrapper<ChannelOrderInfo> channelOrderWrapper=new QueryWrapper<ChannelOrderInfo>();
		channelOrderWrapper.lambda().eq(ChannelOrderInfo::getChannelOrderNo, channelOrderNo);
		channelOrderWrapper.lambda().eq(ChannelOrderInfo::getStatus, 1);
		ChannelOrderInfo channelOrderInfo = channelOrderInfoService.getOne(channelOrderWrapper);
		if(channelOrderInfo==null) {
			return "success";
		}
		QueryWrapper<OrderInfo>orderInfoWrapper=new QueryWrapper<OrderInfo>();
		orderInfoWrapper.lambda().eq(OrderInfo::getSysOrderNo, channelOrderInfo.getSysOrderNo());
		orderInfoWrapper.lambda().eq(OrderInfo::getChannelCode, channelCode);
		orderInfoWrapper.lambda().eq(OrderInfo::getStatus, 1);
		OrderInfo orderInfo=orderInfoService.getOne(orderInfoWrapper);
		//订单不存在不处理
		if(orderInfo==null) {
			return "success";
		}
		ChannelInfo channelInfo = channelInfoService.getById(orderInfo.getChannelId());
		String reStr=channelInfo.getReStr();
		if(reStr==null||reStr.equals("1")) {
			reStr="success,fail";
		}
		String[] split = reStr.split(",");
		String success=split[0];
		String fail=split[1];
		if(channelInfo.getWhiteIp()!=null&&!channelInfo.getWhiteIp().equals(""))       {
			if(!channelInfo.getWhiteIp().contains(ipAddr)) {
				orderFlowService.sendOrderFlow(channelOrderNo,orderInfo.getChannelCode()+"通道通知白名单限制，当前通知地址："+ipAddr);
				return fail;
			}
		}
		Long channelSettingId = orderInfo.getChannelSettingId();
		ChannelSettingInfo channelSettingInfo = channelSettingInfoService.getById(channelSettingId);
		String md5Key = channelSettingInfo.getMd5Key();
		ChannelNoticeDTO noticeDto=new ChannelNoticeDTO();
		noticeDto.setChannelKey(md5Key);
		noticeDto.setChannelCode(channelInfo.getGroupCode());
		noticeDto.setAddress(address);
		noticeDto.setVersion(version);
		noticeDto.setChannelOrderNo(channelOrderNo);
		noticeDto.setResStr(resStr);
		ResultVO resultVO = ChannelUtils.invokeNotice(noticeDto);
		ChannelNoticeRspDTO rspDto= (ChannelNoticeRspDTO) resultVO.getData();
		orderFlowService.sendOrderFlow(channelOrderNo,orderInfo.getChannelCode()+"通道通知微服务返回消息"+JSONUtil.toJsonStr(rspDto));
		if(!rspDto.isVerification()) {
//			log.info("验签失败"+channelOrderInfo.getSysOrderNo());
			orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(),orderInfo.getChannelCode()+"通道通知验签失败"+channelOrderInfo.getSysOrderNo());
			return fail;
		}
		channelOrderInfo.setSerialNumber(rspDto.getSerialNumber());
		orderInfo.setSerialNumber(rspDto.getSerialNumber());
		if(rspDto.getStatus()==2) {
			channelOrderInfo.setStatus(2);
			channelOrderInfo.setResultDate(new Date());
			boolean saveChannelOrderRs = channelOrderInfoService.updateById(channelOrderInfo);
			if(!saveChannelOrderRs) {
				return fail;
			}
			orderInfo.setStatus(2);
			orderInfo.setResultTime(new Date());
			orderInfo.setUpdateTime(new Date());
//			orderInfo.setSerialNumber(dto.getMobileOrderId());
			boolean saveOrderRs = orderInfoService.updateById(orderInfo);
			if(!saveOrderRs) {
				return fail;
			}
			//处理资金
			//订单成功  修改账户资金
			updateAccount(orderInfo, 2, 3);
			rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
		}else if(rspDto.getStatus()==3) {
			channelOrderInfo.setStatus(3);
			channelOrderInfo.setResultDate(new Date());
			boolean saveChannelOrderRs = channelOrderInfoService.updateById(channelOrderInfo);
			if(!saveChannelOrderRs) {
				return fail;
			}
			
			
			
			maxTime=orderInfo.getMaxTime();
			Date createTime = orderInfo.getCreateTime();
			long useTime=(new Date().getTime()-createTime.getTime())/1000;
			if(maxTime-useTime>0) {
				//订单失败  回滚账户资金
				updateAccount(orderInfo, 3, 3);
				if(redisTemplate.hasKey(orderInfo.getSysOrderNo())) {
					orderInfo.setUseTime(useTime);
					orderInfoService.updateById(orderInfo);
					OrderDoubleBookingDto orderDto = (OrderDoubleBookingDto) redisTemplate.opsForValue().get(orderInfo.getSysOrderNo());
					if(!orderDto.getChannelIdList().contains(orderInfo.getChannelId())) {
						orderDto.getChannelIdList().add(orderInfo.getChannelId());
					}
					orderDto.setOrderInfo(orderInfo);
					redisTemplate.opsForValue().set(orderInfo.getSysOrderNo(), orderDto,3, TimeUnit.MINUTES);
					payOrderService.doubliBookingOrder(orderInfo.getSysOrderNo());
				}else {
					OrderDoubleBookingDto orderDto=new OrderDoubleBookingDto();
					orderInfo.setUseTime(useTime);
					orderInfoService.updateById(orderInfo);
					List<Long> channelIdList =new ArrayList<Long>();
					channelIdList.add(orderInfo.getChannelId());
					orderDto.setChannelIdList(channelIdList);
					orderDto.setOrderInfo(orderInfo);
					redisTemplate.opsForValue().set(orderInfo.getSysOrderNo(), orderDto,3, TimeUnit.MINUTES);
					payOrderService.doubliBookingOrder(orderInfo.getSysOrderNo());
				}
			}else {
				//超时，直接处理为失败并回滚商户资金
				orderInfo.setStatus(3);
				orderInfo.setUpdateTime(new Date());
				orderInfo.setResultTime(new Date());
				boolean saveOrderRs = orderInfoService.updateById(orderInfo);
				if(!saveOrderRs) {
					return fail;
				}
				//订单失败  回滚账户资金
				updateAccount(orderInfo, 3, 3);
				//通知商户结果
				rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
			}
		}
		return success;
	}
}
