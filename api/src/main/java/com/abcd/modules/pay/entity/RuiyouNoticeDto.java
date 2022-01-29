package com.abcd.modules.pay.entity;

import lombok.Data;

@Data
public class RuiyouNoticeDto {
	private String orderNo;
	private Long   amt;
	private String dealCode;
	private String dealMsg;
	private long timestamp;
	private String sign;
	private int status;
	private String msg;
	private String serialNumber;
	private String channelCode;
}
