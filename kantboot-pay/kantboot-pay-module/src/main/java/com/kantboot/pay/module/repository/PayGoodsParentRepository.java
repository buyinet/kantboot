package com.kantboot.pay.module.repository;

import com.kantboot.pay.module.entity.PayGoodsParent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PayGoodsParentRepository extends Repository<PayGoodsParent,Long>, CrudRepository<PayGoodsParent,Long> {

    PayGoodsParent findByName(@Param("name") String name);

}
