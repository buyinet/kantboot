package com.kantboot.system.user.module.service;


import com.kantboot.system.user.module.entity.SysArea;
import com.kantboot.util.core.util.PageParam;
import org.springframework.data.domain.Page;

/**
 * 关于地区的业务层
 */
public interface ISysAreaService {

    Page<SysArea> findAll(PageParam<SysArea> pageParam);

}
