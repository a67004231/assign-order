package com.abcd.modules.order.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.abcd.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
 * @author 
 */
@Data
@TableName("channel_order_info")
@ApiModel(value = "通道订单")
public class ChannelOrderInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 通道ID
     */
    private Long channelId;

    /**
     * 通道编码
     */
    private String channelCode;

    /**
     * 下单时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 结果时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date resultDate;

    /**
     * 状态 1充值中 2成功 3失败
     */
    private Integer status;

    /**
     * 是否收到通知 1收到 0未收到
     */
    private Integer isNotify;

    /**
     * 是否查询 1查询 0未查询
     */
    private Integer isSearch;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 系统订单号
     */
    private String sysOrderNo;
    /**
     * 系统订单号
     */
    private String channelOrderNo;
    
    private String tel;
    
    private Long amt;
    
    private Long sysActAmt;
    /**
     * 官方流水号
     */
    private String serialNumber;
    
    /**
     * 版本号
     */
    @Version
    private Integer version;
}