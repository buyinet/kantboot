package com.kantboot.project.util.common.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 这是一个方便用户在rest操作的工具类
 * @author 方某人
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RestResult<T> implements Serializable {

    public static Integer SUCCESS=2000;

    /**
     * 便捷错误码
     */
    public static Integer FALIUD=3333;

    private Integer state;
    private String msg;
    private String errMsg;
    private T data;

    public Boolean getIsSuccess(){
        return SUCCESS.equals(state);
    }

    public RestResult(Integer state) {
        this.state = state;
    }

    public RestResult(Integer state, T data) {
        this.state = state;
        this.data = data;
    }

    public static<T> RestResult<T> success(T data,String msg){
        return new RestResult<T>().setState(RestResult.SUCCESS).setData(data).setMsg(msg);
    }


    public static RestResult error(String msg){
        return new RestResult().setState(RestResult.FALIUD).setErrMsg(msg);
    }

}