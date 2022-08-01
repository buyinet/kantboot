package com.kantboot.system.user.service.impl;


import com.kantboot.project.util.common.exception.BaseException;
import com.kantboot.system.user.entity.SysSetting;
import com.kantboot.system.user.repository.SysSettingRepository;
import com.kantboot.system.user.service.ISysSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysSettingServiceImpl implements ISysSettingService {

    @Resource
    SysSettingRepository repository;

    /**
     * 设置默认角色
     * @param id 角色id
     */
    @Override
    public void setRoleIdByDefault(Long id) {
        SysSetting sysSetting = getSettingBySet();
        sysSetting.setRoleIdByDefault(id);
        repository.save(sysSetting);
    }

    /**
     * 设置默认的微信小程序id
     * @param id
     */
    @Override
    public void setWechatAppletParamIdByDefault(Long id) {
        SysSetting sysSetting = getSettingBySet();
        sysSetting.setAppletWechatParamIdByDefault(id);
        repository.save(sysSetting);
    }

    //    @Override
//    public void setRoleIdByUserJoin(Long roleId) {
//        SysSetting sysSetting = getSettingBySet();
//        sysSetting.setRoleIdByUserJoin(roleId);
//        repository.save(sysSetting);
//    }
//
//    @Override
//    public void setAuthAppletWechatIdByUserJoin(Long authAppletWechatId) {
//        SysSetting sysSetting = getSettingBySet();
//        sysSetting.setAppletWechatIdByUserJoin(authAppletWechatId);
//        repository.save(sysSetting);
//    }
//
//    @Override
//    public void setAuthPayNotifyId(Long authPayNotifyId) {
//        SysSetting sysSetting = getSettingBySet();
//        sysSetting.setAuthPayNotifyId(authPayNotifyId);
//        repository.save(sysSetting);
//    }

    @Override
    public SysSetting getSetting() {
        return repository.findById(1l).get();
    }

    public SysSetting getSettingBySet() {
        synchronized (this.getClass()){
            if(repository.count()==0l){
                throw new BaseException(3333,"没有相应配置，请进行添加");
            }
            return repository.findById(1l).get();
        }
    }

}
