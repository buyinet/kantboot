package com.kantboot.system.user.service;

import com.kantboot.system.user.entity.SysMenu;
import com.kantboot.util.core.util.PageParam;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISysMenuService {

    /**
     * 根据 组织 查询用户
     *
     * @param pageParam
     * @return
     */
    Page<SysMenu> findAll(PageParam<SysMenu> pageParam);

    /**
     * 根据 本身 可以看到的菜单
     */
    Page<SysMenu> findMineList(PageParam<SysMenu> pageParam);

    List<SysMenu> findMineList(SysMenu entity);
}
