package com.kantboot.system.user.module.service;

import com.kantboot.system.user.module.entity.SysDept;
import com.kantboot.util.core.util.PageParam;
import org.springframework.data.domain.Page;

public interface ISysDeptService {

    /**
     * @param pageParam
     * @return
     */
    Page<SysDept> findAll(PageParam<SysDept> pageParam);

    void toAdd(SysDept entity);
}
