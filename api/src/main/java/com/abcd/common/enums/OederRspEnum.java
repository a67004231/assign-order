package com.abcd.common.enums;
/**
 * @Description 会员接口枚举类
 * @Author qiu
 * @Date
 * @Version 1.0
*/
public enum OederRspEnum {
    CODE_10000("10000","订单成功"),
    CODE_10001("10001","订单失败"),
    CODE_10002("10002","充值中"),
	
    //订单类错误
    CODE_20001("20001","运营商错误"),
    CODE_20002("20002","地区错误"),
    CODE_20003("20003","产品信息获取失败"),
    CODE_20004("20004","订单号为空"),
    CODE_20005("20005","手机号为空"),
    CODE_20006("20006","订单号错误，未查询到相应订单"),
    CODE_20007("20007","下单失败"),
    CODE_20008("20008","获取商户产品授权失败"),
    
    
    //通道类错误
    CODE_30001("30001","通道服务异常"),
	CODE_30002("30002","通道授信余额不足"),
	CODE_30003("30003","通道账户信息未配置"),
	CODE_30004("30004","没有可用通道"),
	CODE_30005("30005","通道白名单限制"),
    
	
	//商户类错误
	CODE_40001("40001","获取商户失败"),
	CODE_40002("40002","商户授信余额不足"),
	CODE_40003("40003","商户白名单限制"),
	
	
	//系统类错误
	CODE_90001("90001","验签失败"),
	CODE_90002("90002","保存订单失败");
	
    public final String code;
    public final String desc;

    OederRspEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static OederRspEnum get(String code) {
        OederRspEnum[] vs = OederRspEnum.values();
        for (OederRspEnum apt : vs) {
            if (apt.code.equals(code)) {
                return apt;
            }
        }
        return null;
    }
}
