package com.kantboot.system.user.service;


import com.kantboot.system.user.entity.SysSetting;

public interface ISysSettingService {

//    /**
//     * 设置用户注册时的角色
//     */
//    void setRoleIdByUserJoin(Long roleId);
//
//    void setAuthAppletWechatIdByUserJoin(Long authAppletWechatId);
//
//    void setAuthPayNotifyId(Long authPayNotifyId);

    /**
     * 设置默认角色
     * @param id 角色id
     */
    void setRoleIdByDefault(Long id);

    /**
     * 设置默认的微信小程序参数
     * @param id
     */
    void setWechatAppletParamIdByDefault(Long id);

    /**
     * @return 配置
     */
    SysSetting getSetting();
}
