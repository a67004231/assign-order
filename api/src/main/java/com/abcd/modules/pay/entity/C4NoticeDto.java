package com.abcd.modules.pay.entity;


import lombok.Data;

@Data
public class C4NoticeDto {
	private String sign;
	private String sign_type;
	private String time;
	private String merchant_id;
	private C4NoticeBody body;
	private String code;
	private String channelCode;
}
