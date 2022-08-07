package com.kantboot.system.user.module.service.impl;


import com.kantboot.util.common.exception.BaseException;
import com.kantboot.system.user.module.entity.SysRole;
import com.kantboot.system.user.module.repository.SysRoleRepository;
import com.kantboot.system.user.module.service.ISysRoleService;
import com.kantboot.util.core.util.PageParam;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class SysRoleServiceImpl implements ISysRoleService {

    @Resource
    SysRoleRepository sysRoleRepository;

    @Resource
    SysSettingServiceImpl sysSettingService;

    @Override
    public SysRole setDefaultUse(Long id) {
        SysRole sysRole = sysRoleRepository.findById(id).get();
        sysSettingService.setRoleIdByDefault(sysRole.getId());
        return sysRole;
    }

    @Override
    public SysRole getDefaultUse() {
        SysRole sysRole = sysRoleRepository.findById(sysSettingService.getSetting().getRoleIdByDefaultUse()).get();
        return sysRole;
    }

    @Override
    public List<SysRole> roleByUserJoin() {
        Long roleIdByDefault = sysSettingService.getSetting().getRoleIdByDefaultUse();
        SysRole sysRole = null;
        try{
            sysRole = sysRoleRepository.findById(roleIdByDefault).get();
        }catch (NoSuchElementException e){
            throw new BaseException(3000,"系统未设置默认角色");
        }
        List<SysRole> sysRoles=new ArrayList<>();
        sysRoles.add(sysRole);
        return sysRoles;
    }

    @Override
    public Page<SysRole> findAll(PageParam<SysRole> pageParam) {
        SysRole roles=pageParam.getData();
        Specification<SysRole> specification = new Specification<SysRole>() {
            @Override
            public Predicate toPredicate(Root<SysRole> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                // 如果 名称 不为空，则根据 名称 进行模糊查询
                if(!StringUtils.isEmpty(roles.getName())){
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("name"),
                                            "%" + roles.getName() + "%"));
                }

                // 如果 中文名称 不为空，则根据 中文名称 进行模糊查询
                if(!StringUtils.isEmpty(roles.getZhName())){
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("zhName"),
                                            "%" + roles.getZhName() + "%"));
                }

                // 如果 角色说明 不为空，则根据 角色说明 进行模糊查询
                if(!StringUtils.isEmpty(roles.getContent())){
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("content"),
                                            "%" + roles.getContent() + "%"));
                }

                Predicate[] predicateArr=new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(predicateArr));
            }
        };

        return sysRoleRepository.findAll(specification,pageParam.getPageable());
    }
}
