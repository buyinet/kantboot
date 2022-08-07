package com.kantboot.system.user.module.repository;

import com.kantboot.system.user.module.entity.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface SysRoleRepository extends CrudRepository<SysRole,Long>,
        Repository<SysRole,Long> {

    Page<SysRole> findAll(Specification<SysRole> spec, Pageable pageable);

    SysRole findByName(@Param("name") String name);

}
