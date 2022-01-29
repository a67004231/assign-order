package com.ryhy.provider.dto;

import lombok.Data;

@Data
public class RspDto {
	private String dealCode;
	private String dealMsg;
	private String timestamp;
	private String sign;
}
