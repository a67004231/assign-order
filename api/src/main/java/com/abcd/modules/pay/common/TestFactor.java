package com.abcd.modules.pay.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 测试因子类
 * 这里采用构建者模式构建
 * 优点：1.私有化构造器访问范围小 2.参数可灵活设置便于管理
 * @Author 
 * @CreateTime 2021/4/17 18:59
 */
@Data
public class TestFactor implements Serializable {

    /**
     * 计算类型
     */
    private int type;
    /**
     * 权重
     */
    private Integer weight;
    /**
     * 计算值
     */
    private String value;
    /**
     * 计算因子
     */
    private String factor;


}