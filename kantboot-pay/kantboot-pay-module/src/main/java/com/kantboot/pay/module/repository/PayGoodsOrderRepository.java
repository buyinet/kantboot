package com.kantboot.pay.module.repository;

import com.kantboot.pay.module.entity.PayGoodsOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface PayGoodsOrderRepository extends Repository<PayGoodsOrder,Long>, CrudRepository<PayGoodsOrder,Long> {
}
