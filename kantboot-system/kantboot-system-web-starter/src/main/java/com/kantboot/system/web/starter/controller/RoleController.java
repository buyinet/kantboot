package com.kantboot.system.web.starter.controller;


import com.kantboot.project.util.common.util.RestResult;
import com.kantboot.system.user.entity.SysRole;
import com.kantboot.system.user.service.ISysRoleService;
import com.kantboot.util.core.controller.BaseController;
import com.kantboot.util.core.util.PageParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<SysRole,Long> {

    @Resource
    ISysRoleService roleService;

    @PostMapping("/set_default_use")
    public RestResult<?> setDefaultUse(@RequestParam("id") Long id){
        return RestResult.success(roleService.setDefaultUse(id),"设置成功");
    }

    @PostMapping("/get_default_use")
    public RestResult<?> getDefaultUse(){
        return RestResult.success(roleService.getDefaultUse(),"获取成功");
    }

    /**
     * 查询所有的方法
     * @param pageParam
     * @return
     */
    @PostMapping("/find_all")
    public RestResult<?> findAll(@RequestBody PageParam<SysRole> pageParam){
        return RestResult.success(roleService.findAll(pageParam),"查询成功");
    }

}

