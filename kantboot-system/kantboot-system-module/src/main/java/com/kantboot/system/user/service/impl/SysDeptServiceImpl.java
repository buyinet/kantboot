package com.kantboot.system.user.service.impl;


import com.kantboot.system.user.entity.SysDept;
import com.kantboot.system.user.repository.SysDeptRepository;
import com.kantboot.system.user.service.ISysDeptService;
import com.kantboot.system.user.service.ISysUserService;
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
public class SysDeptServiceImpl implements ISysDeptService {

    @Resource
    SysDeptRepository repository;

    @Resource
    ISysUserService userService;

    @Override
    public Page<SysDept> findAll(PageParam<SysDept> pageParam) {
        SysDept entity=pageParam.getData();
        Specification<SysDept> specification = new Specification<SysDept>() {
            @Override
            public Predicate toPredicate(Root<SysDept> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                if(!StringUtils.isEmpty(entity.getPid())) {
                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("pid"),entity.getPid()));
                }
                if(!StringUtils.isEmpty(entity.getLv())) {
                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("lv"),entity.getLv()));
                }

                // 如果 名称 不为空，则根据 名称 进行模糊查询
                if(!StringUtils.isEmpty(entity.getName())){
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("name"),
                                            "%" + entity.getName() + "%"));
                }


                Predicate[] predicateArr=new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(predicateArr));
            }
        };

        return repository.findAll(specification,pageParam.getPageable());
    }

    @Override
    public void toAdd(SysDept entity) {
        if(entity.getPid()==null){
            entity.setLv(0);
        }else{
            SysDept sysDept1 = repository.findById(entity.getPid()).get();
            entity.setLv(sysDept1.getLv()+1);
        }
        repository.save(entity);
    }
}
