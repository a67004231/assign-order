package com.ryhy.provider.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.ryhy.provider.entity.OrderFlow;
import com.ryhy.provider.service.OrderFlowService;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class OrderFlowServiceImpl implements OrderFlowService {
	@Resource
    RabbitTemplate rabbitTemplate;
	@Value("${order.log.queue}")
	private  String orderFlowQueue;
	@Override
	public void sendOrderFlow(String orderNo, String message) {
		OrderFlow of = new OrderFlow();
		of.setMsg(message);
		of.setOrderNo(orderNo);
		of.setTime(new Date());
		Gson gson = new Gson();  
		String json  = gson.toJson(of);
		try {
			rabbitTemplate.convertAndSend(orderFlowQueue, json);
		} catch (AmqpException  e) {
			e.printStackTrace();
		}

	}

}
