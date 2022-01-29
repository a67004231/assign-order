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
@TableName("mer_account_log")
@ApiModel(value = "商户账户信息日志")
public class MerAccountLog {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 商户ID
     */
    private Long merId;

    /**
     * 资金类型1：授信总余额增加，2：授信总余额减少
3：已使用授信金额增加，4：已使用授信金额减少
5：可用余额增加 6：可用余额减少
7：处理中资金增加 8：处理中资金减少
     */
    private Integer type;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 系统订单号
     */
    private String sysOrderNo;

    /**
     * 变更金额，单位：元
     */
    private Long changeAmt;

    /**
     * 变更前，账户信息（json串）
     */
    private String changeBef;

    /**
     * 变更后账户信息（json串）
     */
    private String changeAft;

    /**
     * 变更备注
     */
    private String changeText;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}