package com.aaarfyh.ranfa.module.service;

import com.aaarfyh.ranfa.module.entity.BusRanfaWork;
import com.kantboot.pay.util.common.util.GoodsParentParam;
import com.kantboot.pay.util.common.util.PayResult;
import com.kantboot.util.core.service.IBaseService;

public interface IBusRanfaWorkService extends IBaseService<BusRanfaWork,Long> {

    /**
     * 随机切换视频
     * @return 返回随机切换的视频
     */
    BusRanfaWork change();

    /**
     * 支付前回调
     * @param param
     * @return
     */
    PayResult payBefore(GoodsParentParam param);
}
