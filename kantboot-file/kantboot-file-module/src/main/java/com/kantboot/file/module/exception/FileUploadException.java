package com.kantboot.file.module.exception;

import com.kantboot.util.common.exception.BaseException;

public class FileUploadException extends BaseException {

    @Override
    public Integer getState() {
        return 7002;
    }

    @Override
    public String getMessage() {
        return "文件上传失败";
    }

}
