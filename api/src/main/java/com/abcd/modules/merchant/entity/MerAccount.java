package com.abcd.modules.merchant.entity;

import com.abcd.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * @author 
 */
@Data
@TableName("mer_account")
@ApiModel(value = "商户账户信息")
public class MerAccount {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 商户ID
     */
    private Long merId;


    /**
     * 授信总金额，单位：元
     */
    private Long creditAmt;

    /**
     * 已使用授信额度，单位：元
     */
    private Long creditUseAmt;

    /**
     * 授信余额，单位：元
     */
    private Long creditBalanceAmt;

    /**
     * 处理中授信资金，单位：元
     */
    private Long creditFixAmt;

    /**
     * 账户状态，1：可用；0：停用
     */
    private Boolean status;
    /**
     * 版本号
     */
    @Version
    private Integer version;
}