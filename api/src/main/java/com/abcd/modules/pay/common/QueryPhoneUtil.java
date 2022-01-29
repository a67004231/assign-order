package com.abcd.modules.pay.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class QueryPhoneUtil {
	/**
     * txt|jsonp|xml
     */
    public static String DATATYPE="jsonp";
    
    public static String get(String urlString,String token) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET"); 
            conn.setRequestProperty("token",token);
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                StringBuilder builder = new StringBuilder();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(),"utf-8"));
                for (String s = br.readLine(); s != null; s = br
                        .readLine()) {
                    builder.append(s);
                }
                br.close();
                return builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static QueryPhoneDto queryPhone(String phone){
        String url="https://api.ip138.com/mobile/?mobile="+phone+"&datatype="+DATATYPE;
        String token="32dd1d01137b4db4b08c83e7d20ccbd9";
        String body = get(url,token);
        log.info(body);
        QueryPhoneDto phoneDto = JSONUtil.toBean(body, QueryPhoneDto.class);
        return phoneDto;
    }
    public static void main(String[] args) {
    	QueryPhoneDto phoneDto = queryPhone("18113181706");
    	
    	System.out.println(phoneDto.getData()[0]);
    	System.out.println(phoneDto.getData()[1]);
    	System.out.println(phoneDto.getData()[2]);
	}
}
