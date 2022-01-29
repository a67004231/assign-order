package com.abcd.config.rabbitmq;

import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;




@Configuration
@ConditionalOnProperty(prefix = "spring.rabbitmq",name = "enable", matchIfMissing = false)
public class RabbtiMqConfig {
	@Value("${order.account.queue}")
	private String updateAccountQueue;
	@Value("${order.notify.queue}")
	private String orderNotifyQueue;
	@Value("${order.notify.delay}")
	private String orderNotifyDelay;
	@Value("${order.search.queue.post}")
	private String orderSearchQueue;
	@Value("${order.search.queue.delay}")
	private String orderSearchDelayQueue;
	@Value("${order.search.queue.exchange}")
	private String directExchange;
    /**
     * 订单查询队列
     * @return
     */
    @Bean
    public Queue dftjDelayQueue(){
        Map<String,Object> params = new HashMap<>();
        params.put("x-dead-letter-directExchange",directExchange);
        params.put("x-dead-letter-routing-key",orderSearchQueue);
        params.put("x-message-ttl", 10000);
        return new Queue(orderSearchDelayQueue,true,false,false,params);
    }
    @Bean
    public Queue notifyDelayQueue(){
    	Map<String,Object> params = new HashMap<>();
    	params.put("x-dead-letter-directExchange",directExchange);
    	params.put("x-dead-letter-routing-key",orderNotifyQueue);
    	params.put("x-message-ttl", 10000);
    	return new Queue(orderNotifyDelay,true,false,false,params);
    }

   

    /**
     * 订单查询队列
     * @return
     */
    @Bean
    public Queue orderSearchQueue(){
        return new Queue(orderSearchQueue,true);
    }
    @Bean
    public Queue orderNotifyQueue(){
    	return new Queue(orderNotifyQueue,true);
    }
    @Bean
    public Queue accountUpdateQueue(){
    	return new Queue(updateAccountQueue,true);
    }

    @Bean
    public Binding dftjQueryBinding(){
        return BindingBuilder.bind(orderSearchQueue()).to(new DirectExchange(directExchange)).with(orderSearchQueue);
    }
}
