package com.abcd.modules.pay.common;


/**
 * 路由因子相关常量
 * @author 
 */
public interface FactorConstant {


    /**
     * 是否通过
     * todo 路由开关，通过则路由通
     */
    int IS_PASS = 1;
    /**
     * 是否通过
     * todo 路由开关，不通过则路由不通
     */
    int IS_NO_PASS = 0;
    /**
     * 是否静态因子
     * todo 静态因子
     */
    int IS_STATIC = 1;
    /**
     * 否静态因子
     * todo 不是静态因子
     */
    int IS_NO_STATIC = 0;
    /**
     * 是否是通道因子
     * todo 是则为通道特有因子
     */
    int IS_CHANNEL = 1;
    /**
     * 是否是通道因子
     * todo 为所有通道因子
     */
    int IS_NO_CHANNEL = 0;
    /**
     * 比较型
     * todo 比较型数值
     */
    int TYPE = 1;
    /**
     * 比较型
     * todo 比较型数值
     */
    int TYPE_1 = 1;
    /**
     * 加分型
     * todo 加分型
     */
    int TYPE_2 = 2;
    /**
     * 数值区间型
     * todo 数值区间型
     */
    int TYPE_3 = 3;
    /**
     * 时间型
     * todo 时间型
     */
    int TYPE_4 = 4;
    /**
     * 大于数值型
     * todo 大于数值型
     */
    int TYPE_5 = 5;
    /**
     * 小于数值型
     * todo 小于数值型
     */
    int TYPE_6 = 6;
    /**
     * 费率区间型
     * todo 费率区间型
     */
    int TYPE_7 = 7;
    /**
     * 库存率区间
     * todo 库存率区间
     */
    int TYPE_8 = 8;
    /**
     * 人工干预通道评分redis主键
     * todo 人工干预通道评分redis主键
     */
    String FACTOR_CHANNEL = "FACTOR_CHANNEL_";
}
