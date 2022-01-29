package com.abcd.modules.pay.entity;

import lombok.Data;

@Data
public class SearchMerAccountDto {
	/**
     * 商户编号
     */
	private String merNo;
	/**
     * 授信总金额，单位：元
     */
    private Long creditAmt;

    /**
     * 已使用授信额度，单位：元
     */
    private Long creditUseAmt;

    /**
     * 授信余额，单位：元
     */
    private Long creditBalanceAmt;

    /**
     * 处理中授信资金，单位：元
     */
    private Long creditFixAmt;
    
    private String sign;
}
