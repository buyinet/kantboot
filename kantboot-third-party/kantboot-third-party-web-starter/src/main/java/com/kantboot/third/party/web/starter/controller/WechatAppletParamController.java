package com.kantboot.third.party.web.starter.controller;

import com.kantboot.util.common.util.RestResult;
import com.kantboot.third.party.module.entity.TpWechatAppletParam;
import com.kantboot.third.party.module.service.ITpWechatAppletParamService;
import com.kantboot.util.core.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/wechat_applet_param")
@RestController
public class WechatAppletParamController extends BaseController<TpWechatAppletParam,Long> {

    @Resource
    ITpWechatAppletParamService service;

    @PostMapping("/set_default_use")
    public RestResult<?> setDefaultUse(@RequestParam("id") Long id){
        return RestResult.success(service.setDefaultUse(id),"设置成功");
    }

}
