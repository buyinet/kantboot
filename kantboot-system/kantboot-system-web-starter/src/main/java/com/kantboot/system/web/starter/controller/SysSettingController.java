package com.kantboot.system.web.starter.controller;

import com.kantboot.project.util.common.util.RestResult;
import com.kantboot.system.user.service.ISysSettingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 关于系统设置的控制器
 */
@RestController
@RequestMapping("/setting")
public class SysSettingController {

    @Resource
    ISysSettingService service;

    /**
     * 设置用户注册时的角色
     */
    @PostMapping("/set_role_id_by_default")
    public RestResult<String> setRoleIdByDefault(@RequestParam("id")Long id){
        service.setRoleIdByDefault(id);
        return RestResult.success("设置成功","设置成功");
    }

//    /**
//     * 设置默认微信小程序
//     * @return
//     */
//    @PostMapping("/set_auth_applet_wechat_id_by_user_join")
//    public RestResult<?> setAuthAppletWechatIdByUserJoin(@RequestParam("authAppletWechatId") Long authAppletWechatId){
//        service.setAuthAppletWechatIdByUserJoin(authAppletWechatId);
//        return RestResult.success("设置成功","设置成功");
//    }
//
//    @PostMapping("/set_auth_pay_notify_id")
//    public RestResult<?> setAuthPayNotifyId(@RequestParam("authPayNotifyId") Long authPayNotifyId){
//        service.setAuthPayNotifyId(authPayNotifyId);
//        return RestResult.success("设置成功","设置成功");
//    }


    @PostMapping("/get_setting")
    public RestResult<?> getSetting(){
        return RestResult.success(service.getSetting(),"获取成功");
    }


}
