package com.kantboot.third.party.wechat.applet.exception;


import com.kantboot.project.util.common.exception.BaseException;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class Code2SessionException extends BaseException {

    private String errMsg;

    @Override
    public Integer getState() {
        return 3301;
    }

    @Override
    public String getMessage() {
        return getErrMsg();
    }


}
