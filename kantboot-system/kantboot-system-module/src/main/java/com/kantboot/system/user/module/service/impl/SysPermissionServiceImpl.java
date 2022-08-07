package com.kantboot.system.user.module.service.impl;


import com.kantboot.system.user.module.entity.SysPermission;
import com.kantboot.system.user.module.repository.SysPermissionRepository;
import com.kantboot.system.user.module.service.ISysPermissionService;
import com.kantboot.util.core.util.PageParam;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysPermissionServiceImpl implements ISysPermissionService {

    @Resource
    SysPermissionRepository sysPermissionRepository;

    @Override
    public Page<SysPermission> findAll(PageParam<SysPermission> pageParam) {
        SysPermission permission=pageParam.getData();
        Specification<SysPermission> specification = new Specification<SysPermission>() {
            @Override
            public Predicate toPredicate(Root<SysPermission> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                // 如果 名称 不为空，则根据 名称 进行模糊查询
                if(!StringUtils.isEmpty(permission.getName())){
                    predicates.add(
                            criteriaBuilder
                            .like(root.get("name"),
                                    "%" + permission.getName() + "%"));
                }

                // 如果 uri 不为空，则根据 uri 进行模糊查询
                if(!StringUtils.isEmpty(permission.getUri())){
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("uri"),
                                            "%" + permission.getUri() + "%"));
                }

                // 如果 中文名称 不为空，则根据 中文名称 进行模糊查询
                if(!StringUtils.isEmpty(permission.getZhName())){
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("zhName"),
                                            "%" + permission.getZhName() + "%"));
                }

                // 如果 是否白名单 不为空，则根据 是否白名单 进行查询
                if(!StringUtils.isEmpty(permission.getMatcher())){
                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("matcher"),permission.getMatcher()));
                }

                // 如果 是否全员通行 不为空，则根据 是否全员通行 进行查询
                if(!StringUtils.isEmpty(permission.getAllPersonCurrent())){
                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("allPersonCurrent"),permission.getAllPersonCurrent()));
                }

                Predicate[] predicateArr=new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(predicateArr));
            }
        };

        return sysPermissionRepository.findAll(specification,pageParam.getPageable());
    }

}
