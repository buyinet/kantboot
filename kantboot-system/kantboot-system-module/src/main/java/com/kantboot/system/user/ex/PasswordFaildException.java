package com.kantboot.system.user.ex;

import com.kantboot.project.util.common.exception.BaseException;

public class PasswordFaildException extends BaseException {

    @Override
    public String getMessage() {
        return "账号或密码错误";
    }

    @Override
    public Integer getState() {
        return 4000;
    }
}
