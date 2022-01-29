package com.abcd.common.exception;

import lombok.Data;

/**
 * @author 
 */
@Data
public class ApiException extends RuntimeException {

    private String msg;

    public ApiException(String msg){
        super(msg);
        this.msg = msg;
    }
}
