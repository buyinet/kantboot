package com.aaarfyh.ranfa.module.service;

import com.aaarfyh.ranfa.module.entity.BusRanfaBrand;
import com.aaarfyh.ranfa.module.entity.BusRanfaWork;
import com.kantboot.pay.util.common.service.IBaseGoodsService;
import com.kantboot.pay.util.common.util.GoodsPayParam;
import com.kantboot.pay.util.common.util.PayResult;

public interface IBusRanfaWorkService extends IBaseGoodsService<BusRanfaWork,Long> {

    /**
     * 随机切换视频
     * @return 返回随机切换的视频
     */
    BusRanfaWork change();

    /**
     * 将品牌放进切换中
     * @return
     */
    BusRanfaWork brandToChange(Long ranfaBrandId);

    /**
     * 将分类放进切换中
     * @param ranfaBrandId
     * @return
     */
    BusRanfaWork techniqueToChange(Long ranfaTechniqueId);

    BusRanfaBrand brandByChange();
    /**
     * 支付前回调
     * @param param
     * @return
     */
    PayResult payBefore(GoodsPayParam param);
}
