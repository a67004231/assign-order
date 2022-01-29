package com.abcd.modules.pay.entity;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class D3NoticeDto {
	private String szAgentId;
	private String szOrderId;
	private String szPhoneNum;
	private String nDemo;
	private String fSalePrice;
	private Integer nFlag;
	private String szRtnMsg;
	private String szVerifyString;
}
