package com.ryhy.base.dto;

import java.io.Serializable;
import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ChannelPayRspDTO implements Serializable{
	/**
     * 订单哈奥
     */
    private String sysOrderNo;
    /**
     * 金额
     */
    private String amount;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 状态1支付中 2成功 3失败
     */
    private int status;
    /**
     * 通道响应描述
     */
    private String channelMsg;
    /**
     * 通道响应编码
     */
    private String channelCode;
    /**
     * 官方通道订单号
     */
    private String serialNumber;
    /**
     * 响应时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date resultTime;
    
    private String thirdOrderNo;
}
