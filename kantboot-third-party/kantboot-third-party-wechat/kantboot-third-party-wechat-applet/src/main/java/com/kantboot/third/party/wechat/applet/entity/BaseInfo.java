package com.kantboot.third.party.wechat.applet.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 当产生错误时的基本类
 */
@Data
@Accessors(chain = true)abstract
public class BaseInfo {

    /**
     * 错误编码
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer errcode;

    /**
     * 错误信息提示
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errmsg;

}
