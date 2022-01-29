package com.abcd.common.enums;
/**
 * @Description 会员接口枚举类
 * @Author qiu
 * @Date
 * @Version 1.0
*/
public enum MemberEnum {
    LOGIN_IN("10000","校验成功"),
    NO_PASSWORD("10001","未设置密码"),
    NEW_REGISTER("10002","新注册用户"),
    ERROR_REGISTER("10003","注册失败");
    public final String code;
    public final String desc;

    MemberEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static MemberEnum get(String code) {
        MemberEnum[] vs = MemberEnum.values();
        for (MemberEnum apt : vs) {
            if (apt.code.equals(code)) {
                return apt;
            }
        }
        return null;
    }
}
