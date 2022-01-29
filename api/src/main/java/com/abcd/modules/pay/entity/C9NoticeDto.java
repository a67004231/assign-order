package com.abcd.modules.pay.entity;

import lombok.Data;

@Data
public class C9NoticeDto {
	private String agentId;
	private String reqStreamId;
	private String evidence;
	private Integer state;
	private String failCode;
	private String sign;
}
