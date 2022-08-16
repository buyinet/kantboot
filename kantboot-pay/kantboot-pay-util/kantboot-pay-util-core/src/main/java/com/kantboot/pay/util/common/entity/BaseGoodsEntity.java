package com.kantboot.pay.util.common.entity;

import com.kantboot.pay.module.entity.PayGoodsBuy;
import com.kantboot.pay.module.entity.PayGoodsCollection;

import java.io.Serializable;


public interface BaseGoodsEntity extends Serializable {

//    Set<PayGoodsInOrder> getPayGoodsInOrders();
    Object setBuy(Boolean entity);
    Boolean getBuy();

    PayGoodsBuy getPayGoodsBuy();

    Object setPayGoodsBuy(PayGoodsBuy entity);

    PayGoodsCollection getPayGoodsCollection();

    Object setPayGoodsCollection(PayGoodsCollection entity);

    Object setCollection(Boolean entity);
    Boolean getCollection();

//    Object setPayGoodsInOrders(Set<PayGoodsInOrder> entity);

}
