package com.kantboot.pay.module.service;


import com.kantboot.pay.module.entity.PayNotify;

public interface IPayNotifyService {

    PayNotify getDefaultUse();
    PayNotify setDefaultUse(Long id);

    /**
     * 回调
     * @return
     */
    Object payNotify();
}
