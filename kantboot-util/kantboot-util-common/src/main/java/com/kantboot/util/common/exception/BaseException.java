package com.kantboot.util.common.exception;

import com.kantboot.util.common.util.RestResult;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理的基类
 */
@Data
@ControllerAdvice
@Accessors(chain = true)
public class BaseException extends RuntimeException{

    private Integer state = 3000;
    private String message;


    public BaseException(){

    }

    public BaseException(Integer state, String message){
        this.state=state;
        this.message=message;
    }

    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public RestResult<String> baseException(HttpServletRequest req, BaseException e){
        return new RestResult<String>().setState(e.getState()).setErrMsg(e.getMessage());
    }



}
