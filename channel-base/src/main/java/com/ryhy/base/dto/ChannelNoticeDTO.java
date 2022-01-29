package com.ryhy.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;


/**
 * RPC接口渠道查询DTO
 * 实现序列化接口
 * @Author 
 * @CreateTime 2021/04/13 16:04
 */
@Data
public class ChannelNoticeDTO implements Serializable {
	private Map<String, String> resMap;
	private String resStr;
	private String channelCode;
	private String channelOrderNo;
	private String appid;
	private String channelKey;
	private String address;
	private String version;
}