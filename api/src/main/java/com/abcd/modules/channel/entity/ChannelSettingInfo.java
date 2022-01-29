package com.abcd.modules.channel.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.abcd.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * @author 
 */
@Data
@TableName("channel_setting_info")
@ApiModel(value = "通道配置")
public class ChannelSettingInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 通道编码
     */
    private String channelCode;

    /**
     * 通道ID
     */
    private Long channelId;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 通道商户编码
     */
    private String appid;

    /**
     * 秘钥类型，1：MD5；2：RSA；3：CFCA证书
     */
    private Integer signType;

    /**
     * MD5秘钥
     */
    private String md5Key;

    /**
     * RSA公钥
     */
    private String rsaPublicKey;

    /**
     * RSA私钥
     */
    private String rsaPrivateKey;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 变更时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 通道状态，1：启用；0：停用；-1：删除
     */
    private Integer status;
    /**
     * 交易密码
     */
    private String tradePwd;
}