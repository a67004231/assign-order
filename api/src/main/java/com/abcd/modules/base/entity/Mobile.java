package com.abcd.modules.base.entity;

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
@TableName("mobile")
@ApiModel(value = "手机号段信息")
public class Mobile {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    
    private String prefix;

    private String mobile;

    private String province;

    private String city;

    private String isp;

    private String areaCode;

    private String postCode;

    private String provinceCode;

    private String cityCode;

    private String lng;

    private String lat;

}