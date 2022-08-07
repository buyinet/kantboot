package com.kantboot.system.web.starter.controller;


import com.kantboot.util.common.util.RestResult;
import com.kantboot.system.user.module.entity.SysMenu;
import com.kantboot.system.user.module.service.ISysMenuService;
import com.kantboot.util.core.controller.BaseController;
import com.kantboot.util.core.util.PageParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  "菜单"的前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController<SysMenu,Long> {

    @Resource
    ISysMenuService service;

    @PostMapping("/find_all")
    public RestResult<?> findAll(@RequestBody PageParam<SysMenu> pageParam){
        return RestResult.success(service.findAll(pageParam),"查询成功");
    }

    @PostMapping("/find_mine")
    public RestResult<?> findMine(@RequestBody PageParam<SysMenu> pageParam){
        return RestResult.success(service.findMineList(pageParam),"查询成功");
    }

    @PostMapping("/find_mine_list")
    public RestResult<?> findMine(@RequestBody SysMenu entity){
        return RestResult.success(service.findMineList(entity),"查询成功");
    }

}

