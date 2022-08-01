package com.kantboot.third.party.wechat.applet.service;


import com.kantboot.third.party.wechat.applet.config.WechatAppletConfig;
import com.kantboot.third.party.wechat.applet.entity.Code2SessionKeyInfo;
import com.kantboot.third.party.wechat.applet.entity.PhoneNumberInfo;
import com.kantboot.third.party.wechat.applet.entity.SessionKeyInfo;
import com.kantboot.third.party.wechat.applet.entity.UserInfo;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Data
@Accessors(chain = true)
@Service
@Lazy
public class InfoService {

    @Resource
    WechatAppletConfig wechatAppletConfig;

    /**
     * 获取小程序前端传来的code解析出 SessionKeyInfo
     * @param code
     * @return
     */
    public SessionKeyInfo createSessionKeyInfo(String code) {
        return new Code2SessionKeyInfo()
                .setAppid(wechatAppletConfig.getAppid())
                .setSecret(wechatAppletConfig.getSecret())
                .setCode(code)
                .createSessionKeyInfo();
    }

    public PhoneNumberInfo createPhoneNumberInfo(
            SessionKeyInfo sessionKeyInfo,
            String encryptedData,
            String iv) {
        return sessionKeyInfo.createPhoneNumberInfo(encryptedData,iv);
    }

    public PhoneNumberInfo createPhoneNumberInfo(String code, String encryptedData, String iv) {
        SessionKeyInfo sessionKeyInfo = new Code2SessionKeyInfo()
                .setAppid(wechatAppletConfig.getAppid())
                .setSecret(wechatAppletConfig.getSecret())
                .setCode(code)
                .createSessionKeyInfo();
        return sessionKeyInfo.createPhoneNumberInfo(encryptedData,iv);
    }

    public UserInfo createUserInfo(SessionKeyInfo sessionKeyInfo, String encryptedData, String iv) {
        return sessionKeyInfo.createUserInfo(encryptedData,iv);
    }

    public UserInfo createUserInfo(String code, String encryptedData, String iv) {
        SessionKeyInfo sessionKeyInfo = new Code2SessionKeyInfo()
                .setAppid(wechatAppletConfig.getAppid())
                .setSecret(wechatAppletConfig.getSecret())
                .setCode(code)
                .createSessionKeyInfo();
        return sessionKeyInfo.createUserInfo(encryptedData,iv);
    }

}
