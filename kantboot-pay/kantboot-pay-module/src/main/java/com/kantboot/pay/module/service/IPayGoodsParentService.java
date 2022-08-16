package com.kantboot.pay.module.service;

import com.kantboot.pay.module.entity.PayGoodsParent;
import com.kantboot.pay.util.common.util.GoodsCollectionParam;
import com.kantboot.pay.util.common.util.GoodsPayParam;
import com.kantboot.util.core.service.IBaseService;

public interface IPayGoodsParentService extends IBaseService<PayGoodsParent,Long> {

    /**
     * 生成订单参数
     * @return
     */
    Object createPayingParam(GoodsPayParam param);

    /**
     * 收藏商品
     * @param param
     * @return
     */
    Object toCollection(GoodsCollectionParam param);

    Object cancelCollection(GoodsCollectionParam param);
}
