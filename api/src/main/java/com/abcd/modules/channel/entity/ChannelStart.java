package com.abcd.modules.channel.entity;

import com.abcd.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;


/**
 * @author 
 */
@Data
@TableName("channel_start")
@ApiModel(value = "通道启动信息")
public class ChannelStart {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 通道id
     */
    private Long channelId;
    /**
     * 通道编码
     */
    private String channelCode;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer orderNum;

    private Integer maxNum;

    /**
     * 库存率比例
     */
    private String scale;

    /**
     * 面额，分为单位
     */
    private Integer amt;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    /**
     * 商户ID
     */
    private Long merId;
    
    /**
     * 运营商名称
     */
    private String serviceName;

    /**
     * 0未启动1启动
     */
    private Integer status;
}