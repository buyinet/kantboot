package com.kantboot.third.party.wechat.applet.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 关于用户基本信息的数据实体类
 */
@Data
@Accessors(chain = true)
public class UserInfo extends BaseInfo implements Serializable {

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
     * 昵称
     */
    private String nickname;

    /**
     * 性别(0 女，1 男)
     */
    private Integer gender;

    /**
     * 城市
     */
    private String city;

    private String province;

    /**
     * 国家
     */
    private String country;

    /**
     * 头像
     */
    private String avatarUrl;

}
