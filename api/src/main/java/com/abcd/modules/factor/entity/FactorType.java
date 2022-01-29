package com.abcd.modules.factor.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.abcd.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * @author 
 */
@Data
@TableName("factor_type")
@ApiModel(value = "路由因子类型")
public class FactorType {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 1比较型，2加分型，3区间型，4时间区间型，5大于,6小于
     */
    private Integer type;

    /**
     * 因子名称
     */
    private String factorName;

    /**
     * 是否通过，1通过0不通过
     */
    private Integer isPass;

    /**
     * 是否是静态因子，1是0不是
     */
    private Integer isStatic;

    /**
     * 是否是关联金额，1是0不是
     */
    private Integer isAmt;

    /**
     * 是否是通道因子，1是0不是
     */
    private Integer isChannel;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态值1启用0未启用2停用
     */
    private Integer status;

    /**
     * 数据键(静态因子与数据字段匹配，动态因子与redis键匹配)
     */
    private String dataKey;

}