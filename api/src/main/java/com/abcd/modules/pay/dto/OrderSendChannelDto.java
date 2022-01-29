package com.abcd.modules.pay.dto;

import java.util.List;

import com.abcd.modules.merchant.entity.MerProductAuth;
import com.abcd.modules.order.entity.OrderInfo;

import lombok.Data;

@Data
public class OrderSendChannelDto {
	List<MerProductAuth> merProductAuthList;
	OrderInfo orderInfo;
	int type;
	MerProductAuth merProductAuth;
}
