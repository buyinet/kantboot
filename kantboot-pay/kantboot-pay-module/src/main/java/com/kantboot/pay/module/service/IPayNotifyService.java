package com.kantboot.pay.module.service;


import com.kantboot.pay.module.entity.PayNotify;

public interface IPayNotifyService {

    PayNotify getDefaultUse();
    PayNotify setDefaultUse(Long id);
}
