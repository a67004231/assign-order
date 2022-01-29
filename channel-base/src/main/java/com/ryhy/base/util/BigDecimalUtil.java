package com.ryhy.base.util;

import java.math.BigDecimal;

/**
 * 资金类计算
 * @ClassName: BigDecimalUtil
 * @Description: TODO
 * @author sunjie
 * @date 2016年10月12日  下午3:38:06
 *
 */
public class BigDecimalUtil {

	/**
	 * 验证金额是否是金额类型的字符串
	 * 
	 * @param str
	 *            金额
	 * @return boolean
	 */
	public static boolean isMoney(String str) {
		String reg = "(^(([1-9]\\d{0,9})|0)(.\\d{0,4})?$)";
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(reg);
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 验证是否是整数
	 * 
	 * @param str
	 *            金额
	 * @return boolean
	 */
	public static boolean isNum(String str) {
		String reg = "(^(([1-9]\\d{0,9})|0))";
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(reg);
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 资金计算验证字符串是否为资金类型
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMoneyForAccount(String str) {
		String reg = "(^(([1-9]\\d{0,18})|0)(.\\d{1,6})?$)";
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(reg);
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 验证金额是否在 0<money<=100000000
	 * 
	 * @param str
	 *            金额
	 * @return boolean
	 */
	public static boolean isAliMoney(String str) {
		if (isMoney(str) == true) {
			Double money = new Double(str);
			if (0 <= money && money <= 100000000) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 加法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static String moneyPluss(String old_money, String pluss) {
		if(!isNullOrEmpty(old_money))old_money="0";
		if(!isNullOrEmpty(pluss))pluss="0";
		if (isNullOrEmpty(old_money) && isNullOrEmpty(pluss) && isMoneyForAccount(old_money)
				&& isMoneyForAccount(pluss)) {
			return new BigDecimal(old_money).add(new BigDecimal(pluss)).toString();
		}
		return "";
	}
	
	/**
	 * 加法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static String moneyPluss(BigDecimal old_money, int pluss) {
		return old_money.add(new BigDecimal(pluss)).toString();
	}
	
	/**
	 * 加法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static String moneyPluss(BigDecimal old_money, String pluss) {
		if(!isNullOrEmpty(pluss))pluss="0";
		if ( isNullOrEmpty(pluss) && isMoneyForAccount(pluss)) {
			return old_money.add(new BigDecimal(pluss)).toString();
		}
		return "";
	}
	
	/**
	 * 加法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static String moneyPluss(BigDecimal old_money, BigDecimal pluss) {
		return old_money.add(pluss).toString();
	}
	
	
	/**
	 * 加法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static BigDecimal moneyPlussd(String old_money, String pluss) {
		if(!isNullOrEmpty(old_money))old_money="0";
		if(!isNullOrEmpty(pluss))pluss="0";
		if (isNullOrEmpty(old_money) && isNullOrEmpty(pluss) && isMoneyForAccount(old_money)
				&& isMoneyForAccount(pluss)) {
			return new BigDecimal(old_money).add(new BigDecimal(pluss));
		}
		return null;
	}
	
	/**
	 * 加法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static BigDecimal moneyPlussd(BigDecimal old_money, int pluss) {
		return old_money.add(new BigDecimal(pluss));
	}
	
	/**
	 * 加法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static BigDecimal moneyPlussd(Long old_money, int pluss) {
		return new BigDecimal(old_money).add(new BigDecimal(pluss));
	}
	
	/**
	 * 加法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static BigDecimal moneyPlussd(BigDecimal old_money, String pluss) {
		if(!isNullOrEmpty(pluss))pluss="0";
		if ( isNullOrEmpty(pluss) && isMoneyForAccount(pluss)) {
			return old_money.add(new BigDecimal(pluss));
		}
		return null;
	}
	
	/**
	 * 加法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static BigDecimal moneyPlussd(BigDecimal old_money, BigDecimal pluss) {
		return old_money.add(pluss);
	}

	/**
	 * 减法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static String moneySubtract(String old_money, String pluss) {
		old_money = isNullOrEmpty(old_money)?old_money:"0";
		pluss = isNullOrEmpty(pluss)?pluss:"0";
		if (isNullOrEmpty(old_money) && isNullOrEmpty(pluss) && isMoneyForAccount(old_money)
				&& isMoneyForAccount(pluss)) {
			return new BigDecimal(old_money).subtract(new BigDecimal(pluss)).toString();
		}
		return "0";
	}
	
	/**
	 * 减法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static BigDecimal moneySubtractd(BigDecimal old_money, int pluss) {
		return old_money.subtract(new BigDecimal(pluss));
	}
	
	/**
	 * 减法
	 * @param old_money
	 * @param pluss
	 * @return
	 */
	public static BigDecimal moneySubtractd(BigDecimal old_money, BigDecimal pluss) {
		return old_money.subtract(pluss);
	}

	/**
	 * 乘法
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	// public static String mul(double d1, double d2) { // 进行乘法运算
	// BigDecimal b1;
	// BigDecimal b2;
	// try {
	// b1 = new BigDecimal(d1);
	// b2 = new BigDecimal(d2);
	// return b1.multiply(b2).toString();
	// } catch (Exception e) {
	// return "0";
	// }
	// }
	/**
	 * 乘法
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String mul(String d1, String d2) { // 进行乘法运算
		BigDecimal b1;
		BigDecimal b2;
		try {
			b1 = new BigDecimal(d1);
			b2 = new BigDecimal(d2);
			return b1.multiply(b2).toString();
		} catch (Exception e) {
			return "0";
		}
	}
	
	/**
	 * 乘法
	 * @Title: muld
	 * @Description: TODO
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal muld(String d1, String d2) { // 进行乘法运算
		BigDecimal b1;
		BigDecimal b2;
		try {
			b1 = new BigDecimal(d1);
			b2 = new BigDecimal(d2);
			return b1.multiply(b2);
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}
	
	public static String mul(BigDecimal d1, String d2) { // 进行乘法运算
		return mul(String.valueOf(d1),d2);
	}
	
	public static String mul(BigDecimal d1, int d2) { // 进行乘法运算
		return mul(String.valueOf(d1),String.valueOf(d2));
	}
	
	/**
	 * 进行乘法运算,结果以字符串输出,并保留指定位数小数
	 * @param d1
	 * @param d2
	 * @param len
	 * @return
	 */
	public static String mul(BigDecimal d1, int d2, int len) { 
		return round(mul(String.valueOf(d1),String.valueOf(d2)),len);
	}
	
	/**
	 * 进行乘法运算,结果以字符串输出
	 * @param d1
	 * @param d2
	 * @param len
	 * @return
	 */
	public static BigDecimal muld(BigDecimal d1, BigDecimal d2) { 
		return new BigDecimal(mul(String.valueOf(d1),String.valueOf(d2)));
	}
	
	/**
	 * 进行乘法运算,结果以字符串输出,并保留指定位数小数
	 * @param d1
	 * @param d2
	 * @param len
	 * @return
	 */
	public static BigDecimal muld(BigDecimal d1, int d2) { 
		return new BigDecimal(mul(String.valueOf(d1),String.valueOf(d2)));
	}
	
	/**
	 * 进行乘法运算,结果以字符串输出,并保留指定位数小数
	 * @param d1
	 * @param d2
	 * @param len
	 * @return
	 */
	public static BigDecimal muld(BigDecimal d1, int d2, int len) { 
		return new BigDecimal(round(mul(String.valueOf(d1),String.valueOf(d2)),len));
	}
	
	/**
	 * 进行乘法运算,结果以字符串输出,并保留指定位数小数
	 * @param d1
	 * @param d2
	 * @param len
	 * @return
	 */
	public static BigDecimal muld(BigDecimal d1, BigDecimal d2, int len) { 
		return new BigDecimal(round(mul(String.valueOf(d1),String.valueOf(d2)),len));
	}
	
	public static String mul(String d1, int d2) { // 进行乘法运算
		return mul(d1,String.valueOf(d2));
	}
	
	/**
	 * 进行乘法运算,结果以字符串输出,并保留指定位数小数
	 * @param d1
	 * @param d2
	 * @param len
	 * @return
	 */
	public static String mul(String d1, int d2, int len) { 
		return round(mul(d1,String.valueOf(d2)),len);
	}

	/**
	 * 除法
	 * 
	 * @param d1
	 *            除数
	 * @param d2
	 *            被除数
	 * @param len
	 *            保留长度
	 * @return
	 */
	// public static String div(double d1, double d2, int len) {// 进行除法运算
	// if(d2==0){
	// return round(d1,len);
	// }
	// BigDecimal b1 = new BigDecimal(d1);
	// BigDecimal b2 = new BigDecimal(d2);
	// return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).toString();
	// }
	/**
	 * 除法
	 * 
	 * @param d1
	 *            除数
	 * @param d2
	 *            被除数
	 * @param len
	 *            保留长度
	 * @return
	 */
	public static String div(String d1, String d2, int len) {// 进行除法运算

		BigDecimal b1;
		BigDecimal b2;
		try {
			b1 = new BigDecimal(d1);
			b2 = new BigDecimal(d2);
			return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).toString();
		} catch (Exception e) {
			return "0";
		}
	}
	
	/**
	 * 除法
	 * 
	 * @param d1
	 *            除数
	 * @param d2
	 *            被除数
	 * @param len
	 *            保留长度
	 * @return
	 */
	public static BigDecimal divd(String d1, String d2, int len) {// 进行除法运算

		BigDecimal b1;
		BigDecimal b2;
		try {
			b1 = new BigDecimal(d1);
			b2 = new BigDecimal(d2);
			return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}
	
	/**
	 * 除法
	 * 
	 * @param d1
	 *            除数
	 * @param d2
	 *            被除数
	 * @param len
	 *            保留长度
	 * @return
	 */
	public static BigDecimal divd(BigDecimal d1, BigDecimal d2, int len) {// 进行除法运算

		BigDecimal b1;
		BigDecimal b2;
		try {
			b1 = d1;
			b2 = d2;
			return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}
	
	/**
	 * 除法
	 * 
	 * @param d1
	 *            除数
	 * @param d2
	 *            被除数
	 * @param len
	 *            保留长度
	 * @return
	 */
	public static BigDecimal divd(BigDecimal d1, int d2, int len) {// 进行除法运算

		BigDecimal b1;
		BigDecimal b2;
		try {
			b1 = d1;
			b2 = BigDecimal.valueOf(d2);
			return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String div(BigDecimal d1 ,int d2){
		return div(d1.toString(),String.valueOf(d2), 2);
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String div(Long d1 ,int d2){
		return div(d1.toString(),String.valueOf(d2), 2);
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String div(Long d1 ,String d2){
		return div(d1.toString(),d2, 2);
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String div(Long d1 ,BigDecimal d2){
		return div(d1.toString(),d2.toString(), 2);
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String div(BigDecimal d1 ,String d2){
		return div(d1.toString(),d2, 2);
	}
	
	/**
	 * 除法,默认保留两位小数 
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String div(String d1 ,int d2){
		return div(d1,String.valueOf(d2), 2);
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String div(String d1 ,String d2){
		return div(d1,d2, 2);
	}
	
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal divd(BigDecimal d1 ,int d2){
		return divd(d1.toString(),String.valueOf(d2), 2);
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal divd(BigDecimal d1 ,BigDecimal d2){
		return divd(d1.toString(),String.valueOf(d2), 2);
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal divd(Long d1 ,int d2){
		return divd(d1.toString(),String.valueOf(d2), 2);
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal divd(Long d1 ,String d2){
		return divd(d1.toString(),d2, 2);
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal divd(Long d1 ,BigDecimal d2){
		return divd(d1.toString(),d2.toString(), 2);
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal divd(BigDecimal d1 ,String d2){
		return divd(d1.toString(),d2, 2);
	}
	
	/**
	 * 除法,默认保留两位小数 
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal divd(String d1 ,int d2){
		return divd(d1,String.valueOf(d2), 2);
	}
	
	/**
	 * 除法,默认保留两位小数
	 * @Title: div
	 * @Description: TODO 除法,默认保留两位小数
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static BigDecimal divd(String d1 ,String d2){
		return divd(d1,d2, 2);
	}

	/**
	 * 计算订单金额 对第len+1位不是0的舍弃，滴len位自动加1
	 * 
	 * @param d1
	 * @param d2
	 * @param len
	 *            保留长度
	 * @return
	 */
	public static String divForCreatOrder(String d1, String d2, int len) {// 进行除法运算
	
		
		BigDecimal b1;
		BigDecimal b2;
		try {
			b1 = new BigDecimal(d1);
			b2 = new BigDecimal(d2);
			BigDecimal re = b1.divide(b2, len + 1, BigDecimal.ROUND_HALF_UP);
			return re.divide(new BigDecimal(1), len, BigDecimal.ROUND_UP).toString();
		} catch (Exception e) {
			return "0";
		}
	}
	
	/**
	 * 直接保留当前位数小数，其他部分全部舍弃 不进只舍 系统给用户付款时使用 如语料分享，
	 * @param d1
	 * @param d2
	 * @param len
	 * @return
	 */
	public static String divForCreatShare(String d1, String d2, int len) {// 进行除法运算
		
		
		BigDecimal b1;
		BigDecimal b2;
		try {
			b1 = new BigDecimal(d1);
			b2 = new BigDecimal(d2);
			BigDecimal re = b1.divide(b2, len + 1, BigDecimal.ROUND_HALF_DOWN);
			return re.divide(new BigDecimal(1), len, BigDecimal.ROUND_HALF_DOWN).toString();
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * 计算计算单位数，直接取整数，若小数部分大于1，则整数部分+1；10.1==11 10.0=10 10.9=11
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String divForUnit(String d1, String d2) {// 进行除法运算

		BigDecimal b1;
		BigDecimal b2;
		try {
			b1 = new BigDecimal(d1);
			b2 = new BigDecimal(d2);
			BigDecimal re = b1.divide(b2, 0, BigDecimal.ROUND_UP);
			return re.toString();
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * 保留len位小数，多余位数直接舍去
	 * 
	 * @param d1
	 * @param d2
	 * @param len
	 * @return
	 */
	public static String divForMaxCoupons(String d1, String d2, int len) {// 进行除法运算

		BigDecimal b1;
		BigDecimal b2;
		try {
			d1= String.valueOf(Math.abs(Double.valueOf(d1)));
			b1 = new BigDecimal(d1);
			b2 = new BigDecimal(d2);
			BigDecimal re = b1.divide(b2, len + 1, BigDecimal.ROUND_HALF_DOWN);
			return re.divide(new BigDecimal(1), len, BigDecimal.ROUND_DOWN).toString();
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * 进行四舍五入
	 * 
	 * @param d
	 *            数字
	 * @param len
	 *            长度
	 * @return
	 */
	public static String round(String d, int len) {
		BigDecimal b1 = new BigDecimal(d);
		BigDecimal b2 = new BigDecimal(1);
		// 任何一个数字除以1都是原数字
		// ROUND_HALF_UP是BigDecimal的一个常量，
		// 表示进行四舍五入的操作
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).toString();
	}

	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.trim().equals("")) {
			return false;
		}
		return true;
	}
}
