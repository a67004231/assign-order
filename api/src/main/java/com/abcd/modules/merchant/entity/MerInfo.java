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
@TableName("mer_info")
@ApiModel(value = "商户信息")
public class MerInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")     
    private Long id;
    
    /**
     * 商户编号
     */
    private String merNo;

    /**
     * 商户全称
     */
    private String merName;

    /**
     * 商户简称
     */
    private String merShortName;

    /**
     * 商户类型，1：企业；2：个体工商户
     */
    private Boolean merType;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 紧急联系人手机号
     */
    private String tel;

    /**
     * 紧急联系人手机号HASH
     */
    private String telHash;

    /**
     * 紧急联系人手机号掩码
     */
    private String telDisplay;

    /**
     * 紧急联系人手机号邮箱
     */
    private String email;

    /**
     * 紧急联系人手机号HASH
     */
    private String emailHash;

    /**
     * 紧急联系人手机号掩码
     */
    private String emailDisplay;

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
     * 商户状态，0：停用；1：启用；
     */
    private Boolean status;
    
    /**
     * 运营商类型 0全网 1移动 2联通 3电信
     */
    private int serviceType;
    
    /**
     * 公司ID
     */
    private Long companyId;
    
    /**
     * 最长时间
     */
    private Long maxTime;
    /**
     * 白名单ip
     */
    private String whiteIp;
}