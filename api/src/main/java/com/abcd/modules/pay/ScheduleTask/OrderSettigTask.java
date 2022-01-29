package com.abcd.modules.pay.ScheduleTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.order.entity.OrderSetting;
import com.abcd.modules.order.service.IOrderInfoService;
import com.abcd.modules.order.service.IOrderSettingService;
import com.abcd.modules.pay.dto.OrderSendChannelDto;
import com.abcd.modules.pay.service.PayOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryhy.base.vo.PayResult;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Configuration      //主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 开启定时任务
@EnableAsync        // 开启多线程
@Slf4j
public class OrderSettigTask {
	@Resource
	private IOrderInfoService orderInfoService;
	@Resource
	private PayOrderService payOrderService;
	@Resource
	private IOrderSettingService orderSettingService;
	@Resource
    RabbitTemplate rabbitTemplate;
	@Value("${order.send.queue}")
	private  String sendChannelQueue;
	@Value("${order.notify.queue}")
	private  String notifyMerQueue;
	@Async
//    @Scheduled(fixedRate=60000)
    public void OrderSettingTask() {
		OrderSetting orderSetting = orderSettingService.getById(1);
		if(orderSetting.getStatus()==1) {
			Integer orderCount = orderSetting.getOrderCount();
			String merIdStr = orderSetting.getMerId();
			String areaIdStr = orderSetting.getAreaId();
			List<Long> areaIdList=new ArrayList<Long>();
			List<Long> merIdList=new ArrayList<Long>();
			QueryWrapper<OrderInfo> orderInfoWrapper=new QueryWrapper<OrderInfo>();
			orderInfoWrapper.lambda().in(OrderInfo::getStatus, -1);
			orderInfoWrapper.lambda().orderByDesc(OrderInfo::getMerRate);
			orderInfoWrapper.lambda().last("limit 0,"+orderCount);
			//判断是否存在优先选择商户或省份的情况
			if(merIdStr!=null&&!merIdStr.equals("")) {
				String[] split = merIdStr.split(",");
				for (String string : split) {
					merIdList.add(Long.valueOf(string));
				}
			}
			if(areaIdStr!=null&&!areaIdStr.equals("")) {
				String[] split = areaIdStr.split(",");
				for (String string : split) {
					areaIdList.add(Long.valueOf(string));
				}
			}
			//根据商户和省份优先设置查询sql
			if(areaIdList.size()>0&&merIdList.size()>0) {
				orderInfoWrapper.lambda().and(wrapper->orderInfoWrapper.lambda().in(OrderInfo::getMerId, merIdList).or().in(OrderInfo::getAreaId, areaIdList));
			}else if(areaIdList.size()>0) {
				orderInfoWrapper.lambda().in(OrderInfo::getAreaId, areaIdList);
			}else if(merIdList.size()>0) {
				orderInfoWrapper.lambda().in(OrderInfo::getMerId, merIdList);
			}
			//如果存在商户或省份优先级则先按商户和省份来查询，订单量不够则按费率排序查询剩余订单。
			if(areaIdList.size()>0||merIdList.size()>0) {
				List<OrderInfo> orderList = orderInfoService.list(orderInfoWrapper);
				for (OrderInfo orderInfo : orderList) {
					//修改订单状态为路由中
					orderInfo.setStatus(0);
					orderInfoService.updateById(orderInfo);
					sendOrder(orderInfo);
				}
				if(orderList.size()<orderCount) {
					orderCount=orderCount-orderList.size();
					QueryWrapper<OrderInfo> orderInfoWrapper2=new QueryWrapper<OrderInfo>();
					orderInfoWrapper2.lambda().in(OrderInfo::getStatus, -1);
					orderInfoWrapper2.lambda().orderByDesc(OrderInfo::getMerRate);
					orderInfoWrapper2.lambda().last("limit 0,"+orderCount);
					List<OrderInfo> orderList2 = orderInfoService.list(orderInfoWrapper2);
					for (OrderInfo orderInfo : orderList2) {
						//修改订单状态为路由中
						orderInfo.setStatus(0);
						orderInfoService.updateById(orderInfo);
						sendOrder(orderInfo);
					}
				}
			}else {
				QueryWrapper<OrderInfo> orderInfoWrapper2=new QueryWrapper<OrderInfo>();
				orderInfoWrapper2.lambda().in(OrderInfo::getStatus, -1);
				orderInfoWrapper2.lambda().orderByDesc(OrderInfo::getMerRate);
				orderInfoWrapper2.lambda().last("limit 0,"+orderCount);
				List<OrderInfo> orderList2 = orderInfoService.list(orderInfoWrapper2);
				for (OrderInfo orderInfo : orderList2) {
					//修改订单状态为路由中
					orderInfo.setStatus(0);
					orderInfoService.updateById(orderInfo);
					sendOrder(orderInfo);
				}
			}
		}else {
			QueryWrapper<OrderInfo> orderInfoWrapper2=new QueryWrapper<OrderInfo>();
			orderInfoWrapper2.lambda().in(OrderInfo::getStatus, -1);
			orderInfoWrapper2.lambda().orderByDesc(OrderInfo::getMerRate);
			List<OrderInfo> orderList2 = orderInfoService.list(orderInfoWrapper2);
			for (OrderInfo orderInfo : orderList2) {
				//修改订单状态为路由中
				orderInfo.setStatus(0);
				orderInfoService.updateById(orderInfo);
				sendOrder(orderInfo);
			}
		}
	}
	@Async
//    @Scheduled(fixedRate=30000)
    public void FailOrderTask() {
		QueryWrapper<OrderInfo> orderInfoWrapper=new QueryWrapper<OrderInfo>();
		String dateTime = DateUtil.offsetSecond(DateUtil.date(), -180).toString("yyyy-MM-dd HH:mm:ss");
		orderInfoWrapper.lambda().le(OrderInfo::getCreateTime, dateTime);
		orderInfoWrapper.lambda().in(OrderInfo::getStatus, -1);
		List<OrderInfo> orderList = orderInfoService.list(orderInfoWrapper);
		for (OrderInfo orderInfo : orderList) {
			orderInfo.setStatus(3);
    		orderInfo.setResultTime(new Date());
    		orderInfo.setUpdateTime(new Date());
    		orderInfoService.updateById(orderInfo);
    		rabbitTemplate.convertAndSend(notifyMerQueue, JSONUtil.toJsonStr(orderInfo));
		}
		
	}
	
	public void sendOrder(OrderInfo orderInfo) {
		OrderSendChannelDto sendDto=new OrderSendChannelDto();
    	sendDto.setMerProductAuth(null);
    	sendDto.setMerProductAuthList(null);
    	sendDto.setOrderInfo(orderInfo);
    	sendDto.setType(1);
    	rabbitTemplate.convertAndSend(sendChannelQueue, JSONUtil.toJsonStr(sendDto));
	}
}
