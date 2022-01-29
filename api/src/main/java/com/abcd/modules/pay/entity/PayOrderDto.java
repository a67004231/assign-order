package com.abcd.modules.pay.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayOrderDto {
	@ApiModelProperty(value="商户编号")
	private String merNo;
	@ApiModelProperty(value="手机号")
	private String phone;
	@ApiModelProperty(value="充值面额")
	private String orderAmount;
	@ApiModelProperty(value="订单号")
	private String orderNo;
	@ApiModelProperty(value="通知商户地址")
	private String notifyUrl;
	@ApiModelProperty(value="交易签名")
	private String sign;
	@ApiModelProperty(value="最长响应时间")
	private Long maxTime;
	@ApiModelProperty(value="充值类型")
	private int type;
	@ApiModelProperty(value="请求IP")
	private String ip;
	@ApiModelProperty(value="城市")
	private String city;
	@ApiModelProperty(value="备注字段")
	private String ext1;
	
}
