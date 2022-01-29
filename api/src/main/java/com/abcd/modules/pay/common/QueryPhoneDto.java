package com.abcd.modules.pay.common;

import lombok.Data;

@Data
public class QueryPhoneDto {
	private String ret;
	private String mobile;
	private String[] data;
}
