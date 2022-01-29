package com.ryhy.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * RPC接口渠道支付DTO
 * 实现序列化接口
 * @Author 
 * @CreateTime 2021/04/13 16:04
 */
@Data
public class ChannelPayDTO implements Serializable {
    /**
     * 订单哈奥
     */
    private String sysOrderNo;
    /**
     * 渠道编码
     */
    private String channelCode;
    /**
     * 渠道账号
     */
    private String appid;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * 渠道名称
     */
    private String channelKey;
    /**
     * 金额
     */
    private String amount;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * dubbo地址
     */
    private String address;
    /**
     * 版本
     */
    private String version;
    /**
     * 运营商编码
     */
    private String productName;
    /**
     * 省份编码
     */
    private String privinceName;
    /**
     * 自动处理结果通道，判断下单成功或者失败
     */
    private int channelType;
    /**
     * 异步回调URL
     */
    private String notifyUrl;
    
    private String ext1;
}