package com.abcd.modules.order.entity;

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
@TableName("err_service_log")
@ApiModel(value = "异常运营商订单日志")
public class ErrServiceLog {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;
    /**
     * 手机号
     */
    private String tel;

    /**
     * 系统订单号
     */
    private String sysOrderNo;

    /**
     * 原始服务商
     */
    private String oldServiceName;

    /**
     * 当前服务商
     */
    private String serviceName;

    /**
     * 创建时间
     */
    private Date createTime;
}