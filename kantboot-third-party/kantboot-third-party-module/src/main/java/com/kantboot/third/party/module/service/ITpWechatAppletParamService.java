package com.kantboot.third.party.module.service;

import com.kantboot.third.party.module.entity.TpWechatAppletParam;

public interface ITpWechatAppletParamService {

    /**
     * 获取默认使用的微信小程序参数
     * @return
     */
    TpWechatAppletParam getDefaultUse();

    /**
     * 设置默认的微信小程序阐述
     * @param id
     * @return
     */
    TpWechatAppletParam setDefaultUse(Long id);


}
