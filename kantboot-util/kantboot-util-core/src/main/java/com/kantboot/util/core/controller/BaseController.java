package com.kantboot.util.core.controller;

import com.alibaba.fastjson.JSON;
import com.kantboot.project.util.common.util.RestResult;
import com.kantboot.util.core.entity.CommonEntity;
import com.kantboot.util.core.entity.CommonEntityPageParam;
import com.kantboot.util.core.exception.UpdateException;
import com.kantboot.util.core.util.FindCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
public abstract class BaseController<T, ID> {

    @Resource
    EntityManagerFactory entityManagerFactory;

    @Resource
    FindCommonUtil<T> findCommonUtil;

    /**
     * 通用查询
     *
     * @return
     */
    @PostMapping("/find_common_list")
    public RestResult<?> findCommonByList(@RequestBody CommonEntity<T> commonEntity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            Class<T> aClass = (Class<T>) commonEntity.getEntity().getClass();
            SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>(aClass, entityManager);

            Specification<T> specification = findCommonUtil.findCommon(commonEntity, entityManager, transaction);
            List<T> all = jpaRepository.findAll(specification);
            transaction.commit();
            entityManager.close();
            return RestResult.success(all, "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return RestResult.error("查询失败");
    }


    /**
     * 通用查询
     *
     * @return
     */
    @PostMapping("/find_common_page")
    public RestResult<?> findCommonByPage(@RequestBody CommonEntityPageParam<T> pageParam) {
        CommonEntity<T> commonEntity = pageParam.getData();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            Class<T> aClass = (Class<T>) commonEntity.getEntity().getClass();
            SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>(aClass, entityManager);
            Specification<T> specification = findCommonUtil.findCommon(commonEntity, entityManager, transaction);
            Page<T> all = jpaRepository.findAll(specification, pageParam.getPageable());
            transaction.commit();
            entityManager.close();
            return RestResult.success(all, "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResult.error("查询失败-catch");
        } finally {
            if(entityManager!=null){
                entityManager.close();
            }
        }
    }

    /**
     * 根据参数中的id删除
     *
     * @param entity
     * @return 删除成功 或者 删除失败
     */
    @PostMapping("/remove")
    public RestResult<?> remove(@RequestBody T entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>((Class<T>) entity.getClass(), entityManager);
        transaction.begin();
        Object o = entityManager.find(entity.getClass(), findCommonUtil.getId(entity));
        try {
            jpaRepository.delete((T) o);
        } catch (Exception e) {
            entityManager.close();
            throw new UpdateException().setMessage("删除失败");
        }
        transaction.commit();
        entityManager.close();
        return RestResult.success("删除成功", "删除成功");
    }

    /**
     * 根据id去查询
     *
     * @param entity
     * @return
     */
    @PostMapping("/find_by_id")
    public RestResult<?> findById(@RequestBody T entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>((Class<T>) entity.getClass(), entityManager);
        Optional<T> byId = jpaRepository.findById((ID) findCommonUtil.getId(entity));
        entityManager.close();
        return RestResult.success(byId, "获取成功");
    }

    /**
     * 添加 或者 修改
     *
     * @param entity 参数，当参数id为null时添加，当参数id不为null时修改
     * @return
     */
    @PostMapping("/save")
    @Transactional
    public RestResult<?> save(@RequestBody T entity) {
        //判断是否为添加，true为添加，false为修改
        boolean isInsert = findCommonUtil.getId(entity) == null;

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>((Class<T>) entity.getClass(), entityManager);

        if (!isInsert) {
            try {
                Optional<T> byId = jpaRepository.findById((ID) findCommonUtil.getId(entity));
                System.out.println("getId(entity) = " + findCommonUtil.getId(entity));
                T entityByInsert = byId.get();
                System.out.println("entityByInsert = " + JSON.toJSONString(entityByInsert));
                BeanUtils.copyProperties(entity, entityByInsert, findCommonUtil.getNullPropertyNames(entity));
                jpaRepository.save(entityByInsert);
            } catch (Exception e) {
                e.printStackTrace();
                entityManager.close();
                throw new UpdateException().setMessage("修改失败");
            }finally {
                transaction.commit();
                entityManager.close();
            }

            return RestResult.success("修改成功", "修改成功");
        }

        try {
            jpaRepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException().setMessage("添加失败");
        }finally {
            transaction.commit();
            entityManager.close();
        }
        return RestResult.success("添加成功", "添加成功");
    }


}
