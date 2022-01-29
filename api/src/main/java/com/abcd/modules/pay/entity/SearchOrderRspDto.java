package com.abcd.modules.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchOrderRspDto {
	@ApiModelProperty(value="商户编号")
	private String merNo;
	@ApiModelProperty(value="交易时间")
	private String orderTime;
	@ApiModelProperty(value="充值面额")
	private Long orderAmount;
	@ApiModelProperty(value="系统订单号")
	private String sysOrderNo;
	@ApiModelProperty(value="商户订单号")
	private String orderNo;
	@ApiModelProperty(value="充值手机号")
	private String phone;
	@ApiModelProperty(value="充值状态")
	private int status;
	@ApiModelProperty(value="充值时间")
	private String resultTime;
	@ApiModelProperty(value="交易结果")
	private String code;
	@ApiModelProperty(value="交易结果描述")
	private String message;
	@ApiModelProperty(value="官方流水号")
	private String serialNumber;
	@ApiModelProperty(value="交易结果签名")
	private String sign;
}	
