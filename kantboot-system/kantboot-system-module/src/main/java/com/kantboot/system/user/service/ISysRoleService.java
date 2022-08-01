package com.kantboot.system.user.service;

import com.kantboot.system.user.entity.SysRole;
import com.kantboot.util.core.util.PageParam;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ISysRoleService {

    Page<SysRole> findAll(PageParam<SysRole> pageParam);

    List<SysRole> roleByUserJoin();

    /**
     * 设置默认角色
     * @return
     */
    SysRole setDefault(Long id);

    /**
     * 获取默认角色
     * @return
     */
    SysRole getDefault();
}
