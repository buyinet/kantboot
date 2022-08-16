package com.kantboot.pay.module.repository;

import com.kantboot.pay.module.entity.PayGoodsCollection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PayGoodsCollectionRepository extends Repository<PayGoodsCollection,Long>, CrudRepository<PayGoodsCollection,Long> {

    PayGoodsCollection findByGoodsIdAndUserIdAndPayGoodsParentName(
            @Param("goodsId") String goodsId,
            @Param("userId") Long userId,
            @Param("payGoodsParentName") String payGoodsParentName
            );
}
