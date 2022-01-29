package com.abcd.common.utils.moneyUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 等额本息计算类
 * @author sunjie
 *
 */
public class PrincipalAndInterestUtil {
	
	/**
	 * 等额本息还款计划
	 * @param totalMoney 总金额
	 * @param periodRate 月利率(年利率/12)
	 * @param period 期数
	 * @return
	 */
	public static List<RepaymentPlanBean> getRepaymentPlan(BigDecimal totalMoney,BigDecimal periodRate,int period) {
		List<RepaymentPlanBean> list = new ArrayList<RepaymentPlanBean>() ;
		Calendar calendar = Calendar.getInstance();
		for (int i = 1; i < period + 1; i++) {
			//每月还款额
			BigDecimal mouthMoney = getMouthMoney(totalMoney,periodRate,period);
			//每月利息
			BigDecimal periodInterest = getPerPeriodInterest(totalMoney,periodRate,period,i);
			//每月本金
			BigDecimal periodPrincipal = BigDecimalUtil.moneySubtractd(mouthMoney, periodInterest);
			calendar.add(calendar.MONTH, 1);
			RepaymentPlanBean bean = new RepaymentPlanBean(calendar.getTime(),mouthMoney,periodPrincipal,periodInterest);
			list.add(bean);
		}
		return list;
	}

	/**
	 * 每月本金+利息
	 * 公式：每月偿还本息=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1)
	 * @param totalMoney 本金
	 * @param periodRate 每期利率
	 * @param period 总期数
	 * @return
	 */
	public static BigDecimal getMouthMoney(BigDecimal totalMoney,BigDecimal periodRate,int period) {
		//1+月利率
		BigDecimal periodRateAddOne = BigDecimalUtil.moneyPlussd(periodRate,1);
		//〔贷款本金×月利率×(1＋月利率)＾还款月数〕
		BigDecimal money1 = BigDecimalUtil.muld(
								BigDecimalUtil.muld(totalMoney, periodRate),
								(periodRateAddOne.pow(period)));
		//〔(1＋月利率)＾还款月数-1)
		BigDecimal money2 = BigDecimalUtil.moneySubtractd(periodRateAddOne.pow(period),1);
		BigDecimal mouthMOney = BigDecimalUtil.divd(money1,money2,2);
		return mouthMOney;
	}
	
	/**
	 * 每期利息
	 * 公式：每月偿还利息=贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
	 * @param totalMoney 本金
	 * @param periodRate 每期利率
	 * @param period 总期数
	 * @param periodNow 当前期数
	 * @return
	 */
	public static BigDecimal getPerPeriodInterest(BigDecimal totalMoney,BigDecimal periodRate,int period,int periodNow) {
		
		//1+月利率
		BigDecimal periodRateAddOne = BigDecimalUtil.moneyPlussd(periodRate,1);
		//贷款本金×月利率
		BigDecimal multiply = BigDecimalUtil.muld(totalMoney, periodRate);
		//(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)
		BigDecimal sub = BigDecimalUtil.moneySubtractd(periodRateAddOne.pow(period),periodRateAddOne.pow(periodNow - 1));
		//每月偿还利息=贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕
		BigDecimal money1 = BigDecimalUtil.muld(multiply,sub);
		//〔(1+月利率)^还款月数-1〕
		BigDecimal money2 = BigDecimalUtil.moneySubtractd(periodRateAddOne.pow(period), 1);
		
		BigDecimal finalMoney = BigDecimalUtil.divd(money1,money2,2);
		return finalMoney;
	}
	
	/**
	 * 每期本金
	 * 每期还款总额-每期利息
	 * @param totalMoney 本金
	 * @param periodRate 每期利率
	 * @param period 总期数
	 * @param periodNow 当前期数
	 * @return
	 */
	public static BigDecimal getPeriodPrincipal(BigDecimal totalMoney,BigDecimal periodRate,int period,int periodNow) {
		//每月还款额
		BigDecimal mouthMoney = getMouthMoney(totalMoney,periodRate,period);
		//每月利息
		BigDecimal periodInterest = getPerPeriodInterest(totalMoney,periodRate,period,periodNow);
		BigDecimal periodPrincipal = BigDecimalUtil.moneySubtractd(mouthMoney, periodInterest);
		return periodPrincipal;
	}
	
	/**
	 * 总利息=月还款额*总期数-总贷款额
	 * @param totalMoney 本金
	 * @param periodRate 每期利率
	 * @param period 总期数
	 * @return
	 */
	public static BigDecimal getTotalInterest(BigDecimal totalMoney,BigDecimal periodRate,int period) {
		BigDecimal mouthMoney = getMouthMoney(totalMoney,periodRate,period);
		BigDecimal TotalInterest = BigDecimalUtil.moneySubtractd(BigDecimalUtil.muld(mouthMoney, period), totalMoney);
		return TotalInterest;
	}

	
}
