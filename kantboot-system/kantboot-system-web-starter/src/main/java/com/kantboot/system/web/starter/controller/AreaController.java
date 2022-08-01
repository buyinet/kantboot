package com.kantboot.system.web.starter.controller;


import com.kantboot.project.util.common.util.RestResult;
import com.kantboot.system.user.entity.SysArea;
import com.kantboot.system.user.service.ISysAreaService;
import com.kantboot.util.core.controller.BaseController;
import com.kantboot.util.core.util.PageParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/area")
public class AreaController extends BaseController<SysArea,Long> {

    @Resource
    ISysAreaService areaService;

    /**
     * 查询所有
     * @param pageParam
     * @return
     */
    @PostMapping("/find_all")
    public RestResult<?> findAll(@RequestBody PageParam<SysArea> pageParam){
        return RestResult.success(areaService.findAll(pageParam),"查询成功");
    }


}
