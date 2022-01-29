package com.abcd.modules.channel.entity;

import com.abcd.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * @author 
 */
@Data
@TableName("channel_mer_auth")
@ApiModel(value = "通道商户授权信息")
public class ChannelMerAuth {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 通道ID
     */
    private Long channelId;

    /**
     * 商户ID
     */
    private Long merId;

    /**
     * 通道编码
     */
    private String channelCode;
    
    /**
     * 商户名称
     */
    private String merName;
    
    /**
     * 状态 1启用 0停用
     */
    private Integer status;
    
    /**
     * 通道名称
     */
    private String channelName;
    /**
     * 费率
     */
    private String rate;
}