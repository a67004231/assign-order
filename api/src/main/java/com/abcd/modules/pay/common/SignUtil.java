package com.abcd.modules.pay.common;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.abcd.modules.pay.entity.PayOrderDto;
import com.abcd.modules.pay.entity.SearchOrderDto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SignUtil {
	public static Boolean verification(Object obj,String signKey,String sign) {
		try {
			String verStr = sign(obj,signKey);
			log.info(verStr);
			return verStr.equalsIgnoreCase(sign);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static Boolean verificationForC4(Object obj,String paramStr,String signKey,String sign) {
		try {
			String verStr = sign(obj,paramStr,signKey);
			log.info(verStr);
			return verStr.equalsIgnoreCase(sign);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static Boolean verificationForC5(String paramStr,String signKey,String sign) {
		try {
			String verStr = signForStr(paramStr,signKey);
			log.info(verStr);
			return verStr.equalsIgnoreCase(sign);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static String ASCII(Object obj) {
		try {
			Map<String, Object> paramMap = BeanUtil.beanToMap(obj);
			TreeMap<String,Object> sort = MapUtil.sort(paramMap);
			StringBuffer sb=new StringBuffer();
			for (Map.Entry<String, Object> entry : sort.entrySet()) {
				String key = entry.getKey().toString();
				if(key.equals("sign")||key.equals("signature")||key.equals("signMethod")) {
					continue;
				}
				if(entry.getValue()==null||"null".equals(entry.getValue().toString())) {
					continue;
				}
				String value = entry.getValue().toString();
				sb.append(key).append("=").append(value).append("&");
			}
			sb.deleteCharAt(sb.length()-1);	
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public static String ASCIIforNull(Object obj) {
		try {
			Map<String, Object> paramMap = BeanUtil.beanToMap(obj);
			TreeMap<String,Object> sort = MapUtil.sort(paramMap);
			StringBuffer sb=new StringBuffer();
			for (Map.Entry<String, Object> entry : sort.entrySet()) {
				String key = entry.getKey().toString();
				if(key.equals("sign")||key.equals("signature")||key.equals("signMethod")) {
					continue;
				}
				String value = entry.getValue()==null?"null":entry.getValue().toString();
				sb.append(key).append("=").append(value).append("&");
			}
			sb.deleteCharAt(sb.length()-1);	
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public static String sign(Object obj,String paramStr,String signKey) {
		try {
			Map<String, Object> paramMap = BeanUtil.beanToMap(obj);
			StringBuffer sb=new StringBuffer();
			if(paramMap.get("code")!=null) {
				sb.append(String.valueOf(paramMap.get("code")));
			}
			sb.append(String.valueOf(paramMap.get("merchant_id")));
			sb.append(paramStr);
			sb.append(String.valueOf(paramMap.get("time")));
			sb.append(signKey);
			System.out.println("签名原串："+sb.toString());
			String verStr = SecureUtil.md5(sb.toString());
			return verStr;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public static String signForStr(String paramStr,String signKey) {
		try {
			StringBuffer sb=new StringBuffer();
			sb.append(paramStr);
			sb.append(signKey);
			System.out.println("签名原串："+sb.toString());
			String verStr = SecureUtil.md5(sb.toString());
			return verStr;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String sign(Object obj,String signKey) {
		try {
			Map<String, Object> paramMap = BeanUtil.beanToMap(obj);
			TreeMap<String,Object> sort = MapUtil.sort(paramMap);
			StringBuffer sb=new StringBuffer();
			for (Map.Entry<String, Object> entry : sort.entrySet()) {
				String key = entry.getKey().toString();
				if(key.equals("sign")||key.equals("ip")||key.equals("signMethod")||key.equals("signature")) {
					continue;
				}
				if(entry.getValue()==null) {
					continue;
				}
				String value = entry.getValue().toString();
				sb.append(key).append("=").append(value).append("&");
			}
			sb.deleteCharAt(sb.length()-1);	
			sb.append(signKey);
			System.out.println(sb);
			String verStr = SecureUtil.md5(sb.toString());
			System.out.println(verStr);
			return verStr;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public static String signForD4(Object obj,String signKey) {
		try {
			Map<String, Object> paramMap = BeanUtil.beanToMap(obj);
			TreeMap<String,Object> sort = MapUtil.sort(paramMap);
			StringBuffer sb=new StringBuffer();
			for (Map.Entry<String, Object> entry : sort.entrySet()) {
				String key = entry.getKey().toString();
				if(key.equals("sign")||key.equals("ip")||key.equals("signMethod")||key.equals("signature")) {
					continue;
				}
				if(entry.getValue()==null||"".equals(entry.getValue())) {
					continue;
				}
				String value ="";
				if(key.equals("vouchers")) {
					value = entry.getValue().toString();
					if(value.equals("[]")) {
						continue;
					}
				}else {
					value = entry.getValue().toString();
				}
				sb.append(key).append("=").append(value).append("&");
			}
			sb.append("key=").append(signKey);
			System.out.println(sb);
			String verStr = SecureUtil.md5(sb.toString()).toUpperCase();
//			System.out.println(verStr);
			return verStr;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public static Boolean verificationForD3(Object obj,String signKey,String sign) {
		try {
			String verStr = signForD3(obj,signKey);
			return verStr.equalsIgnoreCase(sign);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static Boolean verificationForD4(Object obj,String signKey,String sign) {
		try {
			String verStr = signForD4(obj,signKey);
			return verStr.equalsIgnoreCase(sign);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static String signForD3(Object obj,String signKey) {
		try {
			Map<String, Object> paramMap = BeanUtil.beanToMap(obj);
			Map<String,Object> sort = paramMap;
			sort.remove("szVerifyString");
			//胖虎不按字母排序，要按照自己顺序排
//			TreeMap<String,Object> sort = MapUtil.sort(paramMap);
			StringBuffer sb=new StringBuffer();
			for (Map.Entry<String, Object> entry : sort.entrySet()) {
				String key = entry.getKey().toString();
				if(key.equals("sign")||key.equals("szRtnMsg")) {
					continue;
				}
				if(entry.getValue()==null||entry.getValue()=="") {
					continue;
				}
				String value = entry.getValue().toString();
				sb.append(key).append("=").append(value).append("&");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("&szKey=");
			sb.append(signKey);
			System.out.println("加签字段"+sb.toString());
			String verStr = SecureUtil.md5(sb.toString());
			return verStr;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public static void main(String[] args) {
		String md5 = SecureUtil.md5("merOrderId=02c2c5313ab04868acb3e13ccfca7910&merchantId=B2100098283&respCode=1002&respMsg=订单匹配超时，交易失败&success=0&txnAmt=10000ac4f5bc715bf4b0c88a219c7e9b90da");
		System.out.println(md5);
		String encode = Base64Encoder.encode(md5);
		System.out.println(encode);
//		{"maxTime":"300","orderAmount":"50","orderNo":"test00121","phone":"18113181706","sign":"sfsdfsd","noticeUrl":"http://www.baidu.com","merchantNo":"mer001"}
//		PayOrderDto dto=new PayOrderDto();
//		dto.setMaxTime(300l);
//		dto.setMerNo("mer001");
//		dto.setNotifyUrl("http://www.baidu.com");
//		dto.setOrderAmount("50");
//		dto.setOrderNo(new Date().getTime()+"");
//		dto.setPhone("18113181706");
//		dto.setType(1);
//		String sign = sign(dto, "fJESQ9PdJNCvP29L");
//		dto.setSign(sign);
//		System.out.println(JSONUtil.toJsonStr(dto));
//		Boolean verification = verification(dto, "fJESQ9PdJNCvP29L", sign);
//		System.out.println(verification);
	}
}
