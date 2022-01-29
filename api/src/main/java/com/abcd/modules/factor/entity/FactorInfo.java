package com.abcd.modules.factor.entity;

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
@TableName("factor_info")
@ApiModel(value = "路由因子基础配置信息")
public class FactorInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private Long id;

    /**
     * 因子类型id
     */
    private Long factorTypeId;

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 通道编码
     */
    private String channelCode;

    /**
     * 参数值
     */
    private Integer value;

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
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 订单比例
     */
    private double rate;

}