package com.kantboot.system.user.repository;

import com.kantboot.system.user.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SysUserRepository extends Repository<SysUser,Long>, CrudRepository<SysUser,Long>
{

    /**
     * 根据 用户名 查找用户
     * @param username 用户名
     * @return 用户
     */
    SysUser findByUsername(@Param("username")String username);

    /**
     * 根据 条件 查询用户
     * @param spec 查询时的条件
     * @param pageable JPA分页插件
     * @return 查询结果
     */
    Page<SysUser> findAll(Specification<SysUser> spec, Pageable pageable);

    List<SysUser> findAll(Specification<SysUser> spec);

}
