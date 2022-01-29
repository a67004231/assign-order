package com.abcd.modules.channel.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.abcd.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("channel_info")
@ApiModel(value = "通道信息")
public class ChannelInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 通道编码
     */
    private String channelCode;

    
    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 初始权重
     */
    private String weight;

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
     * 通道状态，1：启用；0：停用；-1：删除
     */
    private Boolean status;
    
    /**
     * 查询订单队列名
     */
    private String searchMq;
    /**
     * 通道类型 1普通通道 2统一成功 3统一失败
     */
    private int type;
    /**
     * 分组编码
     */
    private String groupCode;
    /**
     * 回调地址
     */
    private String notifyUrl;
    /**
     * 是否重复下单 1是 0否
     */
    private Integer isRepeat;
    
    /**
     * 公司ID
     */
    private Long companyId;
    /**
     * 公司ID
     */
    @TableField(exist = false)
    private String rate;
    
    /**
     * 响应通知报文
     */
    private String reStr;
    /**
     * 白名单ip
     */
    private String whiteIp;
}