package com.abcd.modules.channel.serviceimpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

import com.abcd.common.lock.RedisLockTemplate;
import com.abcd.common.utils.moneyUtil.BigDecimalUtil;
import com.abcd.modules.channel.entity.ChannelInfo;
import com.abcd.modules.channel.entity.ChannelMerAuth;
import com.abcd.modules.channel.entity.ChannelStart;
import com.abcd.modules.channel.service.IChannelStartService;
import com.abcd.modules.factor.entity.FactorInfo;
import com.abcd.modules.log.service.OrderFlowService;
import com.abcd.modules.mapper.ChannelStartMapper;
import com.abcd.modules.merchant.entity.MerProductAuth;
import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.order.service.IOrderInfoService;
import com.abcd.modules.pay.common.Factor;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 通道启动信息接口实现
 * @author 
 */
@Slf4j
@Service
@Transactional
public class IChannelStartServiceImpl extends ServiceImpl<ChannelStartMapper, ChannelStart> implements IChannelStartService {

    @Resource
    private ChannelStartMapper channelStartMapper;
    @Autowired
    private IOrderInfoService iOrderInfoService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
	private OrderFlowService orderFlowService;
    /**
     * 通道启动
     */
    @Override
    public Factor ChannelStart(OrderInfo orderInfo,List<ChannelInfo> channelList) {
        QueryWrapper<ChannelStart> queryInfoWrapper = new QueryWrapper<>();
//        queryInfoWrapper.lambda().eq(ChannelStart::getChannelId,orderInfo.getChannelId());
        queryInfoWrapper.lambda().eq(ChannelStart::getAmt,Integer.valueOf(orderInfo.getAmt().toString()));
        queryInfoWrapper.lambda().eq(ChannelStart::getStatus,1);
        queryInfoWrapper.lambda().eq(ChannelStart::getServiceName,orderInfo.getServiceName());
        
        Date nowDate = new Date();
        //大于等于
        queryInfoWrapper.lambda().ge(ChannelStart::getEndTime,nowDate);
        
        //小于等于
        queryInfoWrapper.lambda().le(ChannelStart::getStartTime,nowDate);
        List<Long> channelIdList=new ArrayList<Long>();
        for (ChannelInfo channelInfo : channelList) {
        	channelIdList.add(channelInfo.getId());
		}
        queryInfoWrapper.lambda().in(ChannelStart::getChannelId, channelIdList);
        List<ChannelStart> channelStartList = channelStartMapper.selectList(queryInfoWrapper);
        //设置商户ID
        queryInfoWrapper.lambda().eq(ChannelStart::getMerId,orderInfo.getMerId());
        List<ChannelStart> channelStartListForMer = channelStartMapper.selectList(queryInfoWrapper);
        
        ChannelStart channelStart = null;
        if(channelStartListForMer==null||channelStartListForMer.size()==0){
        	if(channelStartList==null||channelStartList.size()==0){
        		return null;
        	}
        }else {
        	channelStartList=channelStartListForMer;
        }
//        log.info("初始化通道列表："+JSONUtil.toJsonStr(channelStartList));
        orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "初始化通道列表： 【"+JSONUtil.toJsonStr(channelStartList)+"】");
        int rand = RandomUtil.randomInt(channelStartList.size());
//        log.info("初始化选择:"+rand);
        orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "初始化选择:"+rand);
        channelStart=channelStartList.get(rand);
        //如果限制为非库存判断则只进行订单数匹配，若为库存型则进行库存比例
        if(channelStart.getScale()==null||"".equals(channelStart.getScale())){
//        	log.info("sysOrderNo: "+orderInfo.getSysOrderNo()+"初始化普通订单比较"+channelStart.getChannelCode());
        	orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "初始化普通订单比较"+channelStart.getChannelCode());
            String sqlStr = "SELECT count(id) " +
                    "from order_info " +
                    "where create_time>=NOW()-INTERVAL 20 SECOND " +
                    "and create_time<=NOW() " +
                    "and amt=" +orderInfo.getAmt().toString() +" "+
                    "and channel_code= '"+channelStart.getChannelCode()+"'";
            String result = iOrderInfoService.queryStringBySql(sqlStr);
//            log.info("sysOrderNo: "+orderInfo.getSysOrderNo()+"初始化普通订单值："+Integer.valueOf(result)/2);
//            log.info("sysOrderNo: "+orderInfo.getSysOrderNo()+"初始化普通最大值："+channelStart.getMaxNum());
            orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "初始化普通订单值："+Integer.valueOf(result)/2);
            orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "初始化普通最大值："+channelStart.getMaxNum());
            //未超过预期值则放行通道
            if(Integer.valueOf(result)/2<channelStart.getMaxNum()){
                Factor factor = new Factor.Builder<>().channelCode(channelStart.getChannelCode()).channelId(channelStart.getChannelId()).total(1L).rate("").build();
                return factor;
            }else {
                return null;
            }
        }
        else {
//        	log.info("sysOrderNo: "+orderInfo.getSysOrderNo()+"初始化库存订单比较");
        	orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "初始化库存订单比较");
            String sqlStr = "SELECT count(id) " +
                    "from order_info " +
                    "where create_time>=NOW()-INTERVAL 20 SECOND " +
                    "and create_time<=NOW()" +
                    "and amt=  " + orderInfo.getAmt()+
                    " and channel_code= '"+channelStart.getChannelCode()+"'";
            String result = iOrderInfoService.queryStringBySql(sqlStr);
//            log.info("sysOrderNo: "+orderInfo.getSysOrderNo()+"初始化库存订单值："+Integer.valueOf(result)/2);
            orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "初始化库存订单值："+Integer.valueOf(result)/2);
            //获取库存率，若没有值则按最大订单数算
            double maxNum = Double.valueOf(channelStart.getMaxNum());
//            log.info("sysOrderNo: "+orderInfo.getSysOrderNo()+"初始化库存最大值："+channelStart.getMaxNum());
            orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "初始化库存最大值："+channelStart.getMaxNum());
            if(redisTemplate.hasKey("kucunshu_"+channelStart.getChannelCode()+"_"+String.valueOf(orderInfo.getAmt()))){
               String redisValue = redisTemplate.opsForValue().get("kucunshu_"+channelStart.getChannelCode()+"_"+String.valueOf(orderInfo.getAmt())).toString();
               Double num = Double.valueOf(BigDecimalUtil.mul(redisValue,channelStart.getScale()));
//               log.info("sysOrderNo: "+orderInfo.getSysOrderNo()+"初始化库存订单redis值："+num);
               orderFlowService.sendOrderFlow(orderInfo.getSysOrderNo(), "初始化库存订单redis值："+num);
               if(maxNum>num){
                   maxNum=num;
               }
               
            }
            //未超过预期值则放行通道
            if(Integer.valueOf(result)/2<maxNum){
                Factor factor = new Factor.Builder<>().channelCode(channelStart.getChannelCode()).channelId(channelStart.getChannelId()).total(1L).rate("").build();
                return factor;
            }else {
                return null;
            }
        }

    }
}