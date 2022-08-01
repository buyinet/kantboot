package com.kantboot.third.party.wechat.applet.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kantboot.third.party.wechat.applet.util.AesCbcUtil;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SessionKeyInfo extends BaseInfo implements Serializable {

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

    private String sessionKey;

    /**
     *  通过解析生成用户在微信的手机号信息
     * @param encryptedData
     * @param iv
     * @return
     */
    @SneakyThrows
    public PhoneNumberInfo createPhoneNumberInfo(String encryptedData,String iv){
        return JSON.parseObject(
                    AesCbcUtil
                        .decrypt(encryptedData,this.sessionKey,iv),
                PhoneNumberInfo.class)
                .setOpenid(this.getOpenid())
                .setUnionid(this.getUnionid());
    }

    /**
     *  通过解析生成用户在微信的用户基本信息
     * @param encryptedData
     * @param iv
     * @return
     */
    @SneakyThrows
    public UserInfo createUserInfo(String encryptedData,String iv){
        return JSON.parseObject(
                AesCbcUtil
                        .decrypt(encryptedData,this.sessionKey,iv),
                UserInfo.class)
                .setOpenid(this.getOpenid())
                .setUnionid(this.getUnionid());
    }


}
