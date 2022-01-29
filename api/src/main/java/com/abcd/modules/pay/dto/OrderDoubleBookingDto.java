package com.abcd.modules.pay.dto;

import java.io.Serializable;
import java.util.List;

import com.abcd.modules.order.entity.OrderInfo;

import lombok.Data;

@Data
public class OrderDoubleBookingDto implements Serializable{
	//系统订单号
	private String sysOrderNo;
	//失败通道ID
	private List<Long> channelIdList; 
	//剩余时间
	private long timeRemaining;
	
	private OrderInfo orderInfo;
}
