package com.kantboot.system.user.module.service.impl;


import com.kantboot.system.user.module.entity.SysArea;
import com.kantboot.system.user.module.service.ISysAreaService;
import com.kantboot.system.user.module.repository.SysAreaRepository;
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
public class SysAreaServiceImpl implements ISysAreaService {

    @Resource
    SysAreaRepository repository;

    @Override
    public Page<SysArea> findAll(PageParam<SysArea> pageParam) {
        SysArea area=pageParam.getData();
        Specification<SysArea> specification = new Specification<SysArea>() {
            @Override
            public Predicate toPredicate(Root<SysArea> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                // 如果 名称 不为空，则根据 名称 进行模糊查询
                if(!StringUtils.isEmpty(area.getName())){
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("name"),
                                            "%" + area.getName() + "%"));
                }

                // 如果 lv 不为空，则根据 lv 查询
                if(!StringUtils.isEmpty(area.getLv())){
                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("lv"), area.getLv()));
                }

                if(!StringUtils.isEmpty(area.getParentIden())){
                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("parentIden"), area.getParentIden()));
                }

                // 如果 lv0Iden 不为空，则根据 lv0Iden 查询
                if(!StringUtils.isEmpty(area.getLv0Iden())){
                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("lv0Iden"), area.getLv0Iden()));
                }

                // 如果 lv1Iden 不为空，则根据 lv1Iden 查询
                if(!StringUtils.isEmpty(area.getLv1Iden())){
                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("lv1Iden"), area.getLv1Iden()));
                }

                // 如果 iden 不为空，则根据 iden 编号查询
                if(!StringUtils.isEmpty(area.getIden())){
                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("iden"), area.getIden()));
                }

                Predicate[] predicateArr=new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(predicateArr));
            }
        };

        return repository.findAll(specification,pageParam.getPageable());
    }
}
