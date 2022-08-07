package com.kantboot.third.party.wechat.pay.exception;


import com.kantboot.util.common.exception.BaseException;

public class NotAppidException extends BaseException {

    @Override
    public Integer getState() {
        return 8003;
    }

    @Override
    public String getMessage() {
        return "缺少参数：appid";
    }
}
