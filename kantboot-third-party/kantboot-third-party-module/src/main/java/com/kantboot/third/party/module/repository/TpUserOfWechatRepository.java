package com.kantboot.third.party.module.repository;

import com.kantboot.third.party.module.entity.TpUserOfWechat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface TpUserOfWechatRepository extends CrudRepository<TpUserOfWechat,Long>, Repository<TpUserOfWechat,Long> {

    TpUserOfWechat findByUnionid(@Param("unionid") String unionid);
    TpUserOfWechat findByUserId(@Param("userId") Long userId);

}
