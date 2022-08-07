package com.kantboot.third.party.module.service;

import com.kantboot.system.user.module.vo.LoginVO;
import com.kantboot.third.party.module.entity.TpUserOfWechat;

public interface ITpUserOfWechatService {

    /**
     * 微信小程序登录
     * @param code
     * @param encryptedData
     * @param iv
     * @return
     */
    LoginVO login(String code, String encryptedData, String iv);

    /**
     * 获取登录的信息
     * @return
     */
    TpUserOfWechat getUserInfo();
}
