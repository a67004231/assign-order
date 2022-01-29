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

import com.abcd.modules.channel.service.IChannelAccountService;
import com.abcd.modules.channel.service.IChannelInfoService;
import com.abcd.modules.merchant.service.IMerAccountService;
import com.abcd.modules.order.entity.OrderInfo;
import com.abcd.modules.pay.dto.AccountUpdateDto;
import com.abcd.modules.pay.service.PayOrderService;
import com.rabbitmq.client.Channel;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class notifyListener {
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
	PayOrderService payOrderService;
	@RabbitListener(queues = "${order.notify.queue}",concurrency = "20")
    @RabbitHandler
    public void process(Channel channel, Message message) {
		try {
	        byte[] body = message.getBody();
	        String msg = new String(body);
	        OrderInfo dto = JSONUtil.toBean(msg, OrderInfo.class);
            // 注* 如果收到的消息中reqType没有值，则需在在此赋值，否则设置为null
            // 原因:参见以前代码 ：SettlePostSubmit
            // 解析
            // 获取业务执业器
            payOrderService.noticeMer(dto);
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
}
