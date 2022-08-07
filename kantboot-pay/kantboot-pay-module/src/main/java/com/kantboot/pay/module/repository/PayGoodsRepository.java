package com.kantboot.pay.module.repository;

import com.kantboot.pay.module.entity.PayGoods;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PayGoodsRepository extends Repository<PayGoods,Long>, CrudRepository<PayGoods,Long> {

    PayGoods findByGoodsIdAndUserIdAndPayGoodsParentName(
            @Param("goodsId") String goodsId,
            @Param("userId") Long userId,
            @Param("payGoodsParentName") String payGoodsParentName
            );
}
