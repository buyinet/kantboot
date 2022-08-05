package com.kantboot.third.party.web.starter.controller;

import com.kantboot.third.party.module.entity.TpWechatPayParam;
import com.kantboot.util.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/wechat_applet_pay_param")
@RestController
public class WechatAppletPayParamController extends BaseController<TpWechatPayParam,Long> {

}
