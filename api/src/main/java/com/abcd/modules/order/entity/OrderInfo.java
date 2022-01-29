package com.abcd.modules.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.abcd.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
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
@TableName("order_info")
@ApiModel(value = "订单信息")
public class OrderInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 系统订单号
     */
    private String sysOrderNo;

    /**
     * 商户订单号
     */
    private String merOrderNo;

    /**
     * 通道订单号
     */
    private String thirdOrderNo;
    /**
     * 官方流水号
     */
    private String serialNumber;

    /**
     * 商户编号
     */
    private String merNo;
    /**
     * 商户id
     */
    private Long merId;

    /**
     * 通道商户编码
     */
    private String thirdMerchantCode;

    /**
     * 通道编码
     */
    private String thirdCode;

    /**
     * 充值手机号
     */
    private String tel;

    /**
     * 运营商，1：移动；2:联通；3:电信
     */
    private Long productId;

    /**
     * 手机号所属省份
     */
    private String province;

    /**
     * 充值金额，单位：元
     */
    private BigDecimal amt;

    /**
     * 系统成本金额，单位：元
     */
    private BigDecimal sysActAmt;
    /**
     *  商户成本金额，单位：元
     */
    private Long merActAmt;

    /**
     * 下单通道扣款，单位：分
     */
    private Long channelAmtAdd;

    /**
     * 成功通道扣款，单位：分
     */
    private Long channelAmtRs;
    /**
     * 下单通道扣款，单位：分
     */
    private Long merAmtAdd;
    
    /**
     * 成功通道扣款，单位：分
     */
    private Long merAmtRs;

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
     * 结果时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date resultTime;

    /**
     * 提交通道时返回结果（json）
     */
    private String postMsg;

    /**
     * 查询或异步返回通道返回结果（json）
     */
    private String asynMsg;

    /**
     * 通道响应码
     */
    private String channelDealCode;

    /**
     * 通道响应描述
     */
    private String channelDealMsg;

    /**
     * 状态，0：受理成功；1：充值中；2：充值成功；3：充值失败
     */
    private int status;

    /**
     * 费率
     */
    private String rate;
    /**
     * 费率
     */
    private String merRate;
    /**
     * 通道编码
     */
    private String channelCode;
    /**
     * 通道ID
     */
    private Long channelId;
    /**
     * 费率类型
     */
    private String rateType;
    /**
     * 手续费
     */
    private BigDecimal fee;
    /**
     * 查询结果次数
     */
    private int searchCount;
    /**
     * 通知商户次数
     */
    private int noticeCount;
    /**
     * 通知商户状态  1成功   2失败 0通知中
     */
    private int noticeStatus;
    /**
     * 通知商户地址
     */
    private String noticeUrl;
    /**
     * 预估时间
     */
    private Long maxTime;
    /**
     * 已使用时间
     */
    private Long useTime;
    
    /**
     * 通道设置ID
     */
    private Long channelSettingId;
    /**
     * 运营商
     */
    private String serviceName;
    /**
     * 产品类型
     */
    private int productType;
    /**
     * 请求IP
     */
    private String ip;
    /**
     * 版本号
     */
    @Version
    private Integer version;
    
    /**
     * 省份ID
     */
    private Long areaId;
    /**
     * 市区ID
     */
    private Long cityId;
    /**
     市区
     */
    private String city;
    
    /**
     * 公司ID
     */
    private Long companyId;
    
    private String ext1;
}