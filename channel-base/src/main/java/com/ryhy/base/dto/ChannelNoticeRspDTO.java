package com.ryhy.base.dto;

import java.io.Serializable;
import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ChannelNoticeRspDTO implements Serializable{
	private int status;
	private boolean verification;
	private String serialNumber;
}
