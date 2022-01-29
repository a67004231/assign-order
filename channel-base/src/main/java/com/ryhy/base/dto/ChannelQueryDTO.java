package com.ryhy.base.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * RPC接口渠道查询DTO
 * 实现序列化接口
 * @Author 
 * @CreateTime 2021/04/13 16:04
 */
@Data
public class ChannelQueryDTO implements Serializable {
    /**
     * 渠道编码
     */
    private String channelCode;
    /**
     * 渠道名称
     */
    private String channelName;
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
     * 订单哈奥
     */
    private String sysOrderNo;
    /**
     * 渠道账号
     */
    private String appid;
    /**
     * 渠道名称
     */
    private String channelKey;
    /**
     * 预估时间
     */
    private Long maxTime;
    /**
     * 已使用时间
     */
    private Long useTime;
    /**
     * 自动处理结果通道，判断下单成功或者失败
     */
    private int channelType;
    /**
     * 通道订单号
     */
    private String thirdOrderNo;
}