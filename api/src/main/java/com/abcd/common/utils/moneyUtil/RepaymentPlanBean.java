package com.abcd.common.utils.moneyUtil;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 还款对象
 * @author sunjie
 *
 */
@Data
public class RepaymentPlanBean {

	//还款日期
	private Date repaymentDate;
	//还款总额
	private BigDecimal totalMoney;
	//还款本金
	private BigDecimal PeriodPrincipal;
	//还款利息
	private BigDecimal PeriodInterest;
	
	public RepaymentPlanBean() {}
	
	public RepaymentPlanBean(Date d,BigDecimal totalMoney,BigDecimal Principal,BigDecimal Interest) {
		this.repaymentDate = d;
		this.totalMoney = totalMoney;
		this.PeriodPrincipal = Principal;
		this.PeriodInterest = Interest;
	} 
}
