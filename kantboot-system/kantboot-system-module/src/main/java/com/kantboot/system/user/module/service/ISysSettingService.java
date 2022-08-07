package com.kantboot.system.user.module.service;


import com.kantboot.system.user.module.entity.SysSetting;
import com.kantboot.util.core.service.IBaseService;

public interface ISysSettingService extends IBaseService<SysSetting,Long> {

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

    void setPayNotifyIdByDefault(Long id);

    /**
     * @return 配置
     */
    SysSetting getSetting();
}
