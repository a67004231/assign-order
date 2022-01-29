package com.abcd.modules.pay.entity;

import java.util.List;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class D6NoticeDto {
	private String ORDER_ID;
	private String ORDER_AMT;
	private String ORDER_TIME;
	private String USER_ID;
	private String BUS_CODE;
	private String PAYCH_TIME;
	private String PAY_AMOUNT;
	private String SIGN_TYPE;
	private String REF_NO;
	private String RESP_CODE;
	private String RESP_DESC;
	private String ADD1;
	private String ADD2;
	private String ADD3;
	private String ADD4;
	private String ADD5;
	private String SIGN;
}
