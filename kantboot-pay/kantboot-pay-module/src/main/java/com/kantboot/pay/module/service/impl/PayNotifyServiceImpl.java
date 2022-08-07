package com.kantboot.pay.module.service.impl;

import com.kantboot.pay.module.entity.PayNotify;
import com.kantboot.pay.module.repository.PayNotifyRepository;
import com.kantboot.pay.module.service.IPayNotifyService;
import com.kantboot.system.user.module.entity.SysSetting;
import com.kantboot.system.user.module.service.ISysSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PayNotifyServiceImpl implements IPayNotifyService {

    @Resource
    ISysSettingService sysSettingService;

    @Resource
    PayNotifyRepository repository;

    @Override
    public PayNotify getDefaultUse() {
        Long authPayNotifyId = sysSettingService.getSetting().getPayNotifyIdByDefault();
        PayNotify result = repository.findById(authPayNotifyId).get();
        return result;
    }

    @Override
    public PayNotify setDefaultUse(Long id) {
        SysSetting setting = sysSettingService.getSetting();
        setting.setPayNotifyIdByDefault(id);
        sysSettingService.save(setting);
        PayNotify result = repository.findById(id).get();
        return result;
    }
}
