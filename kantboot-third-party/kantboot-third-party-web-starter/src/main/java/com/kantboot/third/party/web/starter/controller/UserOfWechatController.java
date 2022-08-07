package com.kantboot.third.party.web.starter.controller;

import com.kantboot.util.common.util.RestResult;
import com.kantboot.system.user.module.vo.LoginVO;
import com.kantboot.third.party.module.service.ITpUserOfWechatService;
import com.kantboot.util.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/user_of_wechat")
@RestController
public class UserOfWechatController extends BaseController {

    @Resource
    ITpUserOfWechatService service;
    /**
     * 微信小程序登录
     * @param code
     * @param encryptedData
     * @param iv
     * @return
     */
    @RequestMapping("/login")
    public RestResult<LoginVO> loginByApplet(
            @RequestParam("code") String code,
            @RequestParam("encryptedData") String encryptedData,
            @RequestParam("iv") String iv){
        return RestResult.success(service.login(code, encryptedData, iv),"登录成功");
    }


}
