package com.abcd.modules.pay.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class YxyNoticeDto implements Serializable{
	private String usr;
	private String ord;
	private String state;
	private String bz;
	private String sgn;
}
