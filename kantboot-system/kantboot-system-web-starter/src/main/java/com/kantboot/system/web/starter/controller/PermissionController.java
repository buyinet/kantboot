package com.kantboot.system.web.starter.controller;


import com.kantboot.util.common.util.RestResult;
import com.kantboot.system.user.module.entity.SysPermission;
import com.kantboot.system.user.module.service.ISysPermissionService;
import com.kantboot.util.core.controller.BaseController;
import com.kantboot.util.core.util.PageParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 权限 管理
 * </p>
 */
@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController<SysPermission,Long> {

    @Resource
    ISysPermissionService sysPermissionService;

    @PostMapping("/find_all")
    public RestResult<?> findAll(@RequestBody PageParam<SysPermission> pageParam){
        return RestResult.success(sysPermissionService.findAll(pageParam),"查询成功");
    }
    
}

