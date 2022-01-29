package com.abcd.modules.pay.entity;

import lombok.Data;

@Data
public class C10NoticeDto {
	private String signMethod;
	private Long   txnAmt;
	private String signature;
	private String success;
	private int respCode;
	private String respMsg;
	private String merchantId;
	private String merOrderId;
	private String mobileOrderId;
	private String channelCode;
}
