package com.abcd.modules.pay.listener;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.abcd.modules.channel.service.IChannelAccountService;
import com.abcd.modules.channel.service.IChannelInfoService;
import com.abcd.modules.channel.service.IChannelSettingInfoService;
import com.abcd.modules.merchant.service.IMerAccountService;
import com.abcd.modules.order.entity.ChannelOrderInfo;
import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.order.service.IChannelOrderInfoService;
import com.abcd.modules.order.service.IOrderInfoService;
import com.abcd.modules.pay.service.PayOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.client.Channel;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderSearchListener {
	@Resource
    private IMerAccountService merAccountService;
    @Resource
    private IChannelAccountService channelAccountService;
	@Resource
    private IChannelInfoService channelInfoService;
	@Resource
    RabbitTemplate rabbitTemplate;
	@Autowired
    private RedisTemplate redisTemplate;
	@Value("${dubbo.registry.address}")
    private  String address;
	@Value("${api.version}")
    private  String version;
	@Autowired
	private IOrderInfoService orderInfoService;
	@Autowired
	private IChannelOrderInfoService channelOrderInfoService;
	@Autowired
	private IChannelSettingInfoService channelSettingInfoService;
	@Autowired
	PayOrderService payOrderService;
	@RabbitListener(queues = "${order.search.queue.post}",concurrency = "20")
    @RabbitHandler
    public void process(Channel channel, Message message) {
		try {
	        byte[] body = message.getBody();
	        String msg = new String(body);
	        ChannelOrderInfo order = JSONUtil.toBean(msg, ChannelOrderInfo.class);
            // 注* 如果收到的消息中reqType没有值，则需在在此赋值，否则设置为null
            // 原因:参见以前代码 ：SettlePostSubmit
            String reqType = "search";
            // 解析
            // 获取业务执业器
            log.info(msg);
            searchOrder(order);
        } catch (Exception e) {
            log.error("[{}]发生异常:{}", e);
        } finally {
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	@Transactional
	private void searchOrder(ChannelOrderInfo channelOrderInfo) {
//		QueryWrapper<OrderInfo>orderInfoWrapper=new QueryWrapper<OrderInfo>();
//		orderInfoWrapper.lambda().eq(OrderInfo::getId, orderInfo.getId());
//		orderInfoWrapper.lambda().eq(OrderInfo::getChannelId, orderInfo.getChannelId());
//		orderInfo=orderInfoService.getOne(orderInfoWrapper);
//		if(orderInfo!=null) {
//			//订单如果不是支付中不作处理
//			if(orderInfo.getStatus()==1||orderInfo.getStatus()==0) {
//				payOrderService.searchChannelOrder(orderInfo);
//			}
//		}
		payOrderService.searchChannelOrder(channelOrderInfo);
	}
}
