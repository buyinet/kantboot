package com.kantboot.util.core.exception;

import com.kantboot.project.util.common.exception.BaseException;

/**
 * 数据库操作失败异常
 */
public class UpdateException extends BaseException {

    @Override
    public Integer getState() {
        return 6000;
    }

    @Override
    public String getMessage() {
        return "操作失败";
    }

}
