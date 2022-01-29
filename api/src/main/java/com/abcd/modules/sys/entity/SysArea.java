package com.abcd.modules.sys.entity;

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
@TableName("sys_area")
@ApiModel(value = "地区信息")
public class SysArea {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域上级标识
     */
    private Integer parentId;

    /**
     * 地名简称
     */
    private String simpleName;

    /**
     * 区域等级
     */
    private Integer level;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 邮政编码
     */
    private String zipCode;

    /**
     * 组合名称
     */
    private String merName;

    /**
     * 经度
     */
    private Float lng;

    /**
     * 纬度
     */
    private Float lat;

    /**
     * 拼音
     */
    private String pinyin;
}