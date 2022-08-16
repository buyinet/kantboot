package com.kantboot.util.core.service;


import com.kantboot.util.core.entity.CommonEntity;
import com.kantboot.util.core.entity.CommonEntityPageParam;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBaseService<T,ID> {

    /**
     * list的通用查询
     * 根据条件查询list
     * @param commonEntity
     * @return
     */
    List<T> findCommonByList(CommonEntity<T> commonEntity);

    /**
     * page的通用查询
     * @param pageParam
     * @return
     */
    Page<T> findCommonByPage(CommonEntityPageParam<T> pageParam);

    T findById(T entity);

    /**
     * 通用删除
     * @param entity
     */
    void remove(T entity);

    void save(T entity);
}
