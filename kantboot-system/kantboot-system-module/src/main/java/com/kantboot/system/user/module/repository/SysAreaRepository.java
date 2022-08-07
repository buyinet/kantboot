package com.kantboot.system.user.module.repository;

import com.kantboot.system.user.module.entity.SysArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;


public interface SysAreaRepository
        extends CrudRepository<SysArea,Long>,
        Repository<SysArea,Long> {

    Page<SysArea> findAll(Specification<SysArea> specification,Pageable pageable);

}
