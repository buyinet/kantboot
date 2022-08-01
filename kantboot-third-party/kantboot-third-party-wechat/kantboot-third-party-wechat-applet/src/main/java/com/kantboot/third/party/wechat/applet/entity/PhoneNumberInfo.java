package com.kantboot.third.party.wechat.applet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 关于用户手机号码基本信息的数据实体类
 */
@Data
@Accessors(chain = true)
public class PhoneNumberInfo extends BaseInfo implements Serializable{

    /**
     * 微信公众平台提供用来识别的一种用户ID
     */
    @JsonIgnore
    private String openid;

    /**
     * 微信开放平台提供用来识别的一种用户ID
     */
    @JsonIgnore
    private String unionid;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 国家编号加手机号
     */
    private String purePhoneNumber;

    /**
     * 国家编号
     */
    private String countryCode;



}
