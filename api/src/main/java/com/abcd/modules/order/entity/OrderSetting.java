package com.abcd.modules.order.entity;

import com.abcd.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
 * @author 
 */
@Data
@TableName("order_setting")
@ApiModel(value = "商户进单设置")
public class OrderSetting {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    
    /**
     * 每分钟进单数量
     */
    private Integer orderCount;

    /**
     * 商户ID
     */
    private String merId;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

    /**
     * 省份ID
     */
    private String areaId;

}