package com.abcd.modules.merchant.entity;

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
@TableName("mer_secret_key")
@ApiModel(value = "商户秘钥信息")
public class MerSecretKey {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    
    /**
     * 商户id
     */
    private Long merId;

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
     * 商户编码
     */
    private String merNo;


}