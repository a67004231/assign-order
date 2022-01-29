package com.ryhy.provider;

import java.util.Map;
import java.util.TreeMap;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SignUtil {
	public static Boolean verification(Object obj,String signKey,String sign) {
		try {
			String verStr = sign(obj,signKey);
			log.info(verStr);
			return verStr.equals(sign);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static String sign(Object obj,String signKey) {
		try {
			Map<String, Object> paramMap = BeanUtil.beanToMap(obj);
			TreeMap<String,Object> sort = MapUtil.sort(paramMap);
			StringBuffer sb=new StringBuffer();
			for (Map.Entry<String, Object> entry : sort.entrySet()) {
				String key = entry.getKey().toString();
				if(key.equals("sign")) {
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
			String verStr = SecureUtil.md5(sb.toString());
			return verStr;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
