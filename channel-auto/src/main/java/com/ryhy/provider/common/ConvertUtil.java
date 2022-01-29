package com.ryhy.provider.common;

public class ConvertUtil {
	public static String convertProvince(String province) {
		String code="";
		switch (province) {
		case "北京":
			code="11";
			break;
		case "天津":
			code="12";
			break;
		case "河北省":
			code="13";
			break;
		case "山西省":
			code="14";
			break;
		case "内蒙古自治区":
			code="15";
			break;
		case "辽宁省":
			code="21";
			break;
		case "吉林省":
			code="22";
			break;
		case "黑龙江省":
			code="23";
			break;
		case "上海":
			code="31";
			break;
		case "江苏省":
			code="32";
			break;
		case "浙江省":
			code="33";
			break;
		case "安徽省":
			code="34";
			break;
		case "福建省":
			code="35";
			break;
		case "江西省":
			code="36";
			break;
		case "山东省":
			code="37";
			break;
		case "河南省":
			code="41";
			break;
		case "湖北省":
			code="42";
			break;
		case "湖南省":
			code="43";
			break;
		case "广东省":
			code="44";
			break;
		case "广西壮族自治区":
			code="45";
			break;
		case "海南省":
			code="46";
			break;
		case "重庆":
			code="50";
			break;
		case "四川省":
			code="51";
			break;
		case "贵州省":
			code="52";
			break;
		case "云南省":
			code="53";
			break;
		case "西藏自治区":
			code="54";
			break;
		case "陕西省":
			code="61";
			break;
		case "甘肃省":
			code="62";
			break;
		case "青海省":
			code="63";
			break;
		case "宁夏回族自治区":
			code="64";
			break;
		case "新疆维吾尔自治区":
			code="65";
			break;

		default:
			break;
		}
		return code;
	}
	
	public static String convertProduct(String productName) {
		String code="";
		switch (productName) {
		case "中国移动":
			code="CZ_YDQ";
			break;
		case "中国联通":
			code="CZ_LTQ";
			break;
		case "中国电信":
			code="CZ_DXQ";
			break;

		default:
			break;
		}
		return code;
	}
}
