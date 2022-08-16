package com.kantboot.pay.module.repository;

import com.kantboot.pay.module.entity.PayGoodsBuy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PayGoodsBuyRepository extends Repository<PayGoodsBuy,Long>, CrudRepository<PayGoodsBuy,Long> {

    PayGoodsBuy findByGoodsIdAndUserIdAndPayGoodsParentName(
            @Param("goodsId") String goodsId,
            @Param("userId") Long userId,
            @Param("payGoodsParentName") String payGoodsParentName
            );
}
