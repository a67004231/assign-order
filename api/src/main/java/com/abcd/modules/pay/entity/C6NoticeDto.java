package com.abcd.modules.pay.entity;

import lombok.Data;

@Data
public class C6NoticeDto {
	private Integer code;
	private String official_order_no;
	private String partner_no;
	private String partner_order_no;
	private String phone;
	private String amount;
	private String isp;
	private String charge_time;
	private String sign;
}
