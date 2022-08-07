package com.kantboot.pay.module.service;

import com.kantboot.pay.module.entity.PayGoodsParent;
import com.kantboot.pay.util.common.util.GoodsParentParam;
import com.kantboot.util.core.service.IBaseService;

public interface IPayGoodsParentService extends IBaseService<PayGoodsParent,Long> {

    /**
     * 生成订单参数
     * @return
     */
    Object createPayingParam(GoodsParentParam param);

}
