package com.kantboot.system.user.repository;

import com.kantboot.system.user.entity.SysMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface SysMenuRepository
        extends Repository<SysMenu,Long>, CrudRepository<SysMenu,Long> {

    Page<SysMenu> findAll(Specification<SysMenu> spec, Pageable pageable);

    List<SysMenu> findAll(Specification<SysMenu> spec);

}
