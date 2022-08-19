package com.kantboot.system.client.service;

import com.kantboot.system.client.entity.SysUser;
import com.kantboot.util.common.util.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("kantboot-system")
public interface SysUserClient {

    @RequestMapping(value = "/user/get_user_info", method = RequestMethod.POST)
    RestResult<SysUser> getUserInfo();

}
