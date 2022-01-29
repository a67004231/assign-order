package com.abcd.modules.pay.common;

import java.security.MessageDigest;
import java.util.Arrays;

public class SignDemo {

    public static void main(String[] args) {
        String account = "test001";
        String secret = "0cd76a5f6e404dc7a6b010a5229241a2";
        String timestamp = "20180828151126";
        String sign = sign(account, secret, timestamp);
        System.out.println("sign:" + sign);
//签名值:9ef2b9994b8664c1544ae646013f6afdf8f1c698
    }

	/**
     * @param account	账号
     * @param secret    密钥
     * @param timestamp 时间戳 格式：yyyyMMddHHmmss
     * @return 签名值
     */
    public static String sign(String account, String secret, String timestamp) {
        String[] arr = new String[]{account, secret, timestamp};
        Arrays.sort(arr);
        String signSource = "";
        for (int i = 0; i < arr.length; i++) {
            signSource += arr[i];
        }
        String signature = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(signSource.getBytes("UTF-8"));
            signature = toHexString(digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return signature;
    }

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(HEX_DIGITS[(bytes[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return sb.toString();
    }
}
