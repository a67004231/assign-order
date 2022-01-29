package com.ryhy.provider.dto;

import lombok.Data;

@Data
public class RspSearchDto {
	private String dealCode;
	private String dealMsg;
	private long timestamp;
	private String sign;
	private int status;
	private String msg;
	private String serialNumber;
}
