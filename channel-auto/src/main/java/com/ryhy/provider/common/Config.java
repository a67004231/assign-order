package com.ryhy.provider.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Config {
	@Value("${order.search.queue.post}")
	private String orderSearchQueue;
	@Value("${order.search.queue.delay}")
	private String orderSearchDelayQueue;
	@Value("${order.search.queue.exchange}")
	private String directExchange;
	
}
