package com.kantboot.third.party.wechat.pay.exception;

import com.kantboot.project.util.common.exception.BaseException;

public class PaymentToUserException extends BaseException {
    @Override
    public Integer getState() {
        return 8004;
    }

    @Override
    public String getMessage() {
        return "提现失败~请联系客服";
    }
}
