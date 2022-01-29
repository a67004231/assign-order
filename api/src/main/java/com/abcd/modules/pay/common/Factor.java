package com.abcd.modules.pay.common;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 计算因子类
 * 这里采用构建者模式构建
 * 优点：1.私有化构造器访问范围小 2.参数可灵活设置便于管理
 * @Author 
 * @CreateTime 2021/4/17 18:59
 */
@Data
public class Factor<T> implements Serializable {

    /**
     * 通道编码
     */
    private String channelCode;
    /**
     * 通道id
     */
    private Long channelId;
    /**
     * 总分数
     */
    private Long total;
    /**
     * 费率
     */
    private String rate;
    /**
     * 因子数据
     */
    private T data;

    /** 私有化构造器  **/
    private Factor() {}
    private Factor(Factor<T> factor) {
        this.channelCode = factor.channelCode;
        this.channelId = factor.channelId;
        this.rate = factor.rate;
        this.total = factor.total;
        this.data = factor.data;
    }

    /**
     * Build
     */
    public static class Builder<T>{
        private Factor<T> factor;
        public Builder() {
            factor = new Factor<>();
        }
        public Builder channelCode(String channelCode){
            factor.channelCode = channelCode;
            return this;
        }
        public Builder channelId(Long channelId){
            factor.channelId = channelId;
            return this;
        }
        public Builder total(Long total){
            factor.total = total;
            return this;
        }
        public Builder rate(String rate){
            factor.rate = rate;
            return this;
        }
        public Builder data(T data){
            factor.data = data;
            return this;
        }
        public Factor<T> build(){
            return new Factor<>(factor);
        }
    }
}