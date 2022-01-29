package com.abcd.modules.auth.entity;

import com.abcd.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 
 */
@Data
@Entity
@Table(name = "sys_client")
@TableName("sys_client")
@ApiModel(value = "客户端信息表")
public class Client extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户端名称")
    private String name;

    @ApiModelProperty(value = "秘钥")
    private String clientSecret;

    @ApiModelProperty(value = "网站主页")
    private String homeUri;

    @ApiModelProperty(value = "成功授权后的回调地址")
    private String redirectUri;

    @ApiModelProperty(value = "版本号")
    private String version;
}