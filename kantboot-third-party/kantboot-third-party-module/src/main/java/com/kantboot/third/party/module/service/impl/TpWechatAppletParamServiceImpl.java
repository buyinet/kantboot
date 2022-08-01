package com.kantboot.third.party.module.service.impl;

import com.kantboot.system.user.service.ISysSettingService;
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
    public TpWechatAppletParam getDefault() {
        Long appletWechatParamIdByDefault = sysSettingService.getSetting().getAppletWechatParamIdByDefault();
        TpWechatAppletParam tpWechatAppletParam = repository.findById(appletWechatParamIdByDefault).get();
        return tpWechatAppletParam;
    }

    @Override
    public TpWechatAppletParam setDefault(Long id) {
        return null;
    }
}
