package com.abcd.modules.pay.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchOrderDto {
	@ApiModelProperty(value="商户编号")
	private String merNo;
	@ApiModelProperty(value="订单号")
	private String orderNo;
	@ApiModelProperty(value="交易签名")
	private String sign;
	
}
