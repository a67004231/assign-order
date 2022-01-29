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
@TableName("mer_product_auth")
@ApiModel(value = "商户秘钥信息")
public class MerProductAuth {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 商户编号
     */
    private Long merId;


    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品编码
     */
    private String productCode;


    /**
     * 费率类型，参见数据字典
     */
    private int rateType;

    /**
     * 费率
     */
    private String rate;

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
     * 通道ID
     */
    private Long channelId;

    /**
     * 通道编码
     */
    private String channelCode;
    
    
}