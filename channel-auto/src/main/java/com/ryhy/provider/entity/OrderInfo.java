package com.ryhy.provider.entity;


import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;


/**
 * @author 
 */
@Data
public class OrderInfo {

    private static final long serialVersionUID = 1L;

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
     * 通道实扣余额，单位：元
     */
    private BigDecimal channelActAmt;

    /**
     * 通道可用余额，单位：元
     */
    private BigDecimal channelBalance;

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
}