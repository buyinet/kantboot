package com.kantboot.system.user.module.repository;

import com.kantboot.system.user.module.entity.SysPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface SysPermissionRepository
        extends CrudRepository<SysPermission,Long>,
        Repository<SysPermission,Long>
{

    /**
     * 根据uri查询
      */
    SysPermission findByUri(String uri);

    List<SysPermission> findByMatcherIsTrue();

    List<SysPermission> findByAllPersonCurrentIsTrue();


    Page<SysPermission> findAll(Specification<SysPermission> spec, Pageable pageable);

}
