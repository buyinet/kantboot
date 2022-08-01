package com.kantboot.system.user.service;

import com.kantboot.system.user.entity.SysPermission;
import com.kantboot.util.core.util.PageParam;
import org.springframework.data.domain.Page;

public interface ISysPermissionService {

    Page<SysPermission> findAll(PageParam<SysPermission> pageParam);

}
