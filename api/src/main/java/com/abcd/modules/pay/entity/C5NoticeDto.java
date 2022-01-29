package com.abcd.modules.pay.entity;


import lombok.Data;

@Data
public class C5NoticeDto {
	private String order_id;
	private String order_no;
	private String order_cert_no;
	private String paid;
	private String order_status;
	private String amount;
	private String phone_no;
	private String time_paid;
	private String cookie;
}
