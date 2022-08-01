package com.kantboot.third.party.module.repository;

import com.kantboot.third.party.module.entity.TpWechatAppletParam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface TpWechatAppletParamRepository extends CrudRepository<TpWechatAppletParam,Long>, Repository<TpWechatAppletParam,Long> {
}
