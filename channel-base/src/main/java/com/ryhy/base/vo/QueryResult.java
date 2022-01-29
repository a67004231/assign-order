package com.ryhy.base.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回结果类
 * 这里采用构建者模式构建
 * 优点：1.私有化构造器访问范围小 2.参数可灵活设置便于管理
 * @Author Sans
 * @CreateTime 2019/11/7 18:59
 */
@Data
public class QueryResult implements Serializable {

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回信息
     */
    private String message;

}