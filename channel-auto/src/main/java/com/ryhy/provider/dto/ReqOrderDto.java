package com.ryhy.provider.dto;

import lombok.Data;

@Data
public class ReqOrderDto {
	private String service;
	private String version;
	private String merchantNo;
	private String orderNo;
	private String productCode;
	private String tel;
	private String privinceCode;
	private Long amt;
	private String curCode;
	private String orderTime;
	private String notifyUrl;
	private String sign;
}
