package com.kantboot.system.web.starter.controller;


import com.kantboot.project.util.common.util.RestResult;
import com.kantboot.system.user.entity.SysDept;
import com.kantboot.system.user.service.ISysDeptService;
import com.kantboot.util.core.controller.BaseController;
import com.kantboot.util.core.util.PageParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * "组织的前端控制器"控制器
 */
@RestController
@RequestMapping("/dept")
public class DeptController extends BaseController<SysDept,Long> {

    @Resource
    ISysDeptService service;

    @PostMapping("/find_all")
    public RestResult<?> findAll(@RequestBody PageParam<SysDept> pageParam){
        return RestResult.success(service.findAll(pageParam),"查询成功");
    }

    /**
     * 添加
     * @param entity
     * @return
     */
    @PostMapping("/to_add")
    public RestResult<?> toAdd(@RequestBody SysDept entity){
        service.toAdd(entity);
        return RestResult.success("添加成功","添加成功");
    }
}
