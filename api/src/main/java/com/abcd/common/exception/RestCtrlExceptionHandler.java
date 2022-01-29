package com.abcd.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.abcd.common.utils.ResultUtil;
import com.abcd.common.vo.Result;

/**
 * @author 
 */
@Slf4j
@RestControllerAdvice
public class RestCtrlExceptionHandler {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Result<Object> handleXCloudException(ApiException e) {

        String errorMsg="API exception";
        if (e!=null){
            errorMsg = e.getMsg();
            log.error(e.toString(), e);
        }
        return new ResultUtil<>().setErrorMsg(500, errorMsg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Result<Object> handleException(Exception e) {

        String errorMsg="Exception";
        if (e!=null){
            errorMsg = e.getMessage();
            log.error(e.toString(), e);
        }
        return new ResultUtil<>().setErrorMsg(500, errorMsg);
    }
}
