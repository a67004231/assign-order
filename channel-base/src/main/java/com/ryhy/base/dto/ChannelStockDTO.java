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
public class ChannelStockDTO implements Serializable {
    /**
     * 渠道编码
     */
    private String channelCode;
    /**
     * 渠道名称
     */
    private String channelName;
    /**
     * dubbo地址
     */
    private String address;
    /**
     * 版本
     */
    private String version;
    
    /**
     * 渠道账号
     */
    private String appid;
    /**
     * 渠道秘钥
     */
    private String channelKey;

}