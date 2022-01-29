package com.abcd.modules.channel.entity;

import java.util.Date;

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
@TableName("mer_channel_route")
@ApiModel(value = "商户通道路由")
public class MerChannelRoute {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 商户id
     */
    private Long merId;

    /**
     * 商户编码
     */
    private String merNo;

    /**
     * 商户名
     */
    private String merName;

    /**
     * 通道id
     */
    private Long channelId;

    /**
     * 通道编码
     */
    private String channelCode;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 公司名
     */
    private String companyName;

    /**
     * 运营商
     */
    private String operators;

    /**
     * 区域id
     */
    private String areaIds;

    /**
     * 面额
     */
    private String hasAmout;

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态0未启用1启用2停用
     */
    private Integer status;

    private Integer sort;
}