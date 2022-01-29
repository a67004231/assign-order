package com.abcd.modules.factor.entity;

import com.abcd.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author 
 */
@Data
@TableName("factor_log")
@ApiModel(value = "因子计算日志")
public class FactorLog {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 系统订单号
     */
    private String sysOrderNo;

    /**
     * 商户订单号
     */
    private String merOrderNo;

    /**
     * 电话号
     */
    private String tel;
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 商户编号
     */
    private String merNo;
    /**
     * 商户id
     */
    private Long merId;
    /**
     * 通道编码
     */
    private String channelCode;
    /**
     * 通道id
     */
    private Long channelId;
    /**
     * 分数
     */
    private Long total;
    /**
     * 费率
     */
    private String rate;
    /**
     * 金额
     */
    private BigDecimal amt;
    /**
     * 因子名称
     */
    private String factorName;
    /**
     * 因子id
     */
    private Long factorId;
    /**
     * 参数值
     */
    private String value;
    /**
     * 最小数据
     */
    private Integer minData;

    /**
     * 最大数据
     */
    private Integer maxData;

    /**
     * 权重（0-100）
     */
    private Long weight;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最终通道通道编码
     */
    private String lastChannel;
    /**
     * 步骤
     */
    private int step;
}