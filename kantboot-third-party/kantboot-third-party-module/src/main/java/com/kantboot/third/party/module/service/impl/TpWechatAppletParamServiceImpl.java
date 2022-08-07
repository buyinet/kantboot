package com.kantboot.third.party.module.service.impl;

import com.kantboot.system.user.module.entity.SysSetting;
import com.kantboot.system.user.module.service.ISysSettingService;
import com.kantboot.third.party.module.entity.TpWechatAppletParam;
import com.kantboot.third.party.module.repository.TpWechatAppletParamRepository;
import com.kantboot.third.party.module.service.ITpWechatAppletParamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TpWechatAppletParamServiceImpl implements ITpWechatAppletParamService {

    @Resource
    ISysSettingService sysSettingService;

    @Resource
    TpWechatAppletParamRepository repository;

    @Override
    public TpWechatAppletParam getDefaultUse() {
        Long appletWechatParamIdByDefault = sysSettingService.getSetting().getAppletWechatParamIdByDefault();
        TpWechatAppletParam tpWechatAppletParam = repository.findById(appletWechatParamIdByDefault).get();
        return tpWechatAppletParam;
    }

    @Override
    public TpWechatAppletParam setDefaultUse(Long id) {
        SysSetting sysSetting = sysSettingService.getSetting().setAppletWechatParamIdByDefault(id);
        sysSettingService.setWechatAppletParamIdByDefault(id);
        TpWechatAppletParam tpWechatAppletParam = repository.findById(id).get();
        return tpWechatAppletParam;
    }
}
