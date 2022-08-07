package com.kantboot.system.user.module.repository;

import com.kantboot.system.user.module.entity.SysDept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface SysDeptRepository extends CrudRepository<SysDept,Long>, Repository<SysDept,Long> {

    Page<SysDept> findAll(Specification<SysDept> spec, Pageable pageable);

}
