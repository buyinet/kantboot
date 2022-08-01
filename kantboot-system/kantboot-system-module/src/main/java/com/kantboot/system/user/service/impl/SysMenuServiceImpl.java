package com.kantboot.system.user.service.impl;


import com.kantboot.system.user.entity.SysMenu;
import com.kantboot.system.user.entity.SysRole;
import com.kantboot.system.user.entity.SysUser;
import com.kantboot.system.user.repository.SysMenuRepository;
import com.kantboot.system.user.service.ISysMenuService;
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
public class SysMenuServiceImpl implements ISysMenuService {

    @Resource
    SysMenuRepository repository;
    @Resource
    ISysUserService userService;

    @Override
    public Page<SysMenu> findAll(PageParam<SysMenu> pageParam) {
        SysMenu entity=pageParam.getData();
        Specification<SysMenu> specification = new Specification<SysMenu>() {
            @Override
            public Predicate toPredicate(Root<SysMenu> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                predicates.add(
                        criteriaBuilder
                                .isNull(root.get("pid")));
                // 如果 标签 不为空，则根据 标签 进行模糊查询
                if(!StringUtils.isEmpty(entity.getLabel())){
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("label"),
                                            "%" + entity.getLabel() + "%"));
                }

                // 如果 名称 不为空，则根据 名称 进行模糊查询
                if(!StringUtils.isEmpty(entity.getName())){
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("name"),
                                            "%" + entity.getName() + "%"));
                }

                // 如果 路径 不为空，则根据 路径 进行模糊查询
                if(!StringUtils.isEmpty(entity.getName())){
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("path"),
                                            "%" + entity.getPath() + "%"));
                }

                Predicate[] predicateArr=new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(predicateArr));
            }
        };

        return repository.findAll(specification,pageParam.getPageable());
    }

    @Override
    public Page<SysMenu> findMineList(PageParam<SysMenu> pageParam) {
        SysMenu entity=pageParam.getData();
        Specification<SysMenu> specification = new Specification<SysMenu>() {
            @Override
            public Predicate toPredicate(Root<SysMenu> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                SysUser userInfo = userService.getUserInfo();

                if(!(StringUtils.isEmpty(userInfo.getRoles()))){
                    List<SysRole> roles = userInfo.getRoles();
                    for (int i = 0; i < roles.size(); i++) {
                        SysRole role = roles.get(i);
                        if(role!=null&&role.getId()!=null){
                            predicates.add(
                                    criteriaBuilder
                                            .equal(root.join("roles").get("id"),role.getId())
                            );
                        }

                    }
                }

                Predicate[] predicateArr=new Predicate[predicates.size()];
                Predicate or = criteriaBuilder.or(predicates.toArray(predicateArr));
                List<Predicate> predicates2 = new ArrayList<Predicate>();
                predicates2.add(
                        criteriaBuilder
                                .isNull(root.get("pid"))
                );
                Predicate[] predicateArr2=new Predicate[predicates2.size()];
                Predicate and = criteriaBuilder.and(predicates.toArray(predicateArr2));

                return  criteriaQuery.where(or,and).getRestriction();
            }
        };

        return repository.findAll(specification,pageParam.getPageable());

    }

    @Override
    public List<SysMenu> findMineList(SysMenu entity) {
        SysUser userInfo = userService.getUserInfo();
        Specification<SysMenu> specification = new Specification<SysMenu>() {
            @Override
            public Predicate toPredicate(Root<SysMenu> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(
                        criteriaBuilder
                                .isNull(root.get("pid"))
                );


                if(!(StringUtils.isEmpty(userInfo.getRoles()))){
                    List<SysRole> roles = userInfo.getRoles();
                    for (int i = 0; i < roles.size(); i++) {
                        SysRole role = roles.get(i);
                        if(role!=null&&role.getId()!=null){
                            predicates.add(
                                    criteriaBuilder
                                            .equal(root.join("roles").get("id"),role.getId())
                            );
                        }

                    }
                }

                Predicate[] predicateArr=new Predicate[predicates.size()];
                Predicate or = criteriaBuilder.or(predicates.toArray(predicateArr));
                List<Predicate> predicates2 = new ArrayList<Predicate>();
                predicates2.add(
                        criteriaBuilder
                                .isNull(root.get("pid"))
                );
                Predicate[] predicateArr2=new Predicate[predicates2.size()];
                Predicate and = criteriaBuilder.and(predicates.toArray(predicateArr2));

                return  criteriaQuery.where(or,and).getRestriction();
            }
        };
        List<SysMenu> all = repository.findAll(specification);
        List<SysRole> roles1 = userInfo.getRoles();
        for (SysMenu sysMenu : all) {
            List<SysMenu> ch=new ArrayList<>();
            Specification<SysMenu> specification1 = new Specification<SysMenu>() {
                @Override
                public Predicate toPredicate(Root<SysMenu> root,
                                             CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<Predicate>();

                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("pid"),sysMenu.getId())
                    );


                    if(!(StringUtils.isEmpty(userInfo.getRoles()))){
                        List<SysRole> roles = userInfo.getRoles();
                        for (int i = 0; i < roles.size(); i++) {
                            SysRole role = roles.get(i);
                            if(role!=null&&role.getId()!=null){
                                predicates.add(
                                        criteriaBuilder
                                                .equal(root.join("roles").get("id"),role.getId())
                                );
                            }

                        }
                    }

                    Predicate[] predicateArr=new Predicate[predicates.size()];
                    Predicate or = criteriaBuilder.or(predicates.toArray(predicateArr));
                    List<Predicate> predicates2 = new ArrayList<Predicate>();
                    predicates2.add(
                            criteriaBuilder
                                    .equal(root.get("pid"),sysMenu.getId())
                    );

                    Predicate[] predicateArr2=new Predicate[predicates2.size()];
                    Predicate and = criteriaBuilder.and(predicates.toArray(predicateArr2));
                    //按照优先级排序
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("priority")));

                    return  criteriaQuery.where(or,and).getRestriction();
                }
            };

            ch=repository.findAll(specification1);
//
//            List<SysMenu> children = sysMenu.getChildren();
//            for (SysMenu child : children) {
//                List<SysRole> roles = child.getRoles();
//                Boolean addBool=false;
//                for (SysRole role : roles) {
//                    for (SysRole sysRole : roles1) {
//                        if(role.getId().equals(sysRole.getId())){
//                            addBool=true;
//                        }
//                    }
//                }
//                if(addBool){
//                    ch.add(child);
//                }
//
//            }
            sysMenu.setChildren(ch);
        }
        return all;

    }
}
