package com.kantboot.pay.util.common.entity;

import com.kantboot.pay.module.entity.PayGoods;

public interface BaseGoodsEntity{

//    Set<PayGoodsInOrder> getPayGoodsInOrders();

    PayGoods getPayGoods();

    Object setPayGoods(PayGoods entity);

//    Object setPayGoodsInOrders(Set<PayGoodsInOrder> entity);

}
