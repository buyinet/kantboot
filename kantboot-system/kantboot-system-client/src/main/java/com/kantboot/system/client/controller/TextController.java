package com.kantboot.system.client.controller;

import com.kantboot.system.client.service.SysUserClient;
import com.kantboot.util.common.util.RestResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TextController {

    @Resource
    SysUserClient userClient;

    @RequestMapping("/get_user_info")
    public RestResult<?> getUserInfo(){
//        System.out.println("userClient.getUserInfo() = " + userClient.getUserInfo());
        return userClient.getUserInfo();
    }
}
