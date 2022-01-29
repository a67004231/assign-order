package com.abcd.modules.product.entity;

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
@TableName("product_info")
@ApiModel(value = "地区信息")
public class ProductInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 产品编码
     */
    private String code;

    /**
     * 产品类型，参见数据字典
     */
    private int type;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 上级产品编码
     */
    private String parentCode;

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
     * 状态，1：启用；0：停用；-1：删除
     */
    private Boolean status;

    /**
     * 地区编码
     */
    private Long areaId;
}