package com.abcd.modules.company.entity;

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
@TableName("company_info")
@ApiModel(value = "公司信息")
public class CompanyInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 公司编码
     */
    private String companyNo;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    /**
     * 路由类型：1只能路由 2普通路由
     */
    private Integer routeType;
}