package com.abcd.modules.base.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 
 */
@Data
@AllArgsConstructor
public class RedisVo {

    private String key;

    private String value;
}
