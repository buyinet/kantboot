package com.kantboot.pay.module.repository;

import com.kantboot.pay.module.entity.PayGoodsInOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface PayGoodsInOrderRepository extends Repository<PayGoodsInOrder,Long>, CrudRepository<PayGoodsInOrder,Long> {
}
