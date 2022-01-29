package com.abcd.modules.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayOrderRspDto {
	@ApiModelProperty(value="系统订单号")
	private String sysOrderNo;
	@ApiModelProperty(value="商户订单号")
	private String orderNo;
	@ApiModelProperty(value="交易结果")
	private String code;
	@ApiModelProperty(value="交易结果描述")
	private String message;
	@ApiModelProperty(value="成本金额")
	private String sysActAmount;
	@ApiModelProperty(value="交易结果签名")
	private String sign;
}	
