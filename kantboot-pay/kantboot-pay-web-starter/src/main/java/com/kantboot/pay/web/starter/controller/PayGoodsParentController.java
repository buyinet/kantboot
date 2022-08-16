package com.kantboot.pay.web.starter.controller;

import com.kantboot.pay.module.entity.PayGoodsParent;
import com.kantboot.pay.module.service.IPayGoodsParentService;
import com.kantboot.pay.util.common.util.GoodsCollectionParam;
import com.kantboot.pay.util.common.util.GoodsPayParam;
import com.kantboot.util.common.util.RestResult;
import com.kantboot.util.core.controller.BaseController;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Log4j2
@RequestMapping("/pay_goods_parent")
public class PayGoodsParentController
        extends BaseController<PayGoodsParent,Long> {
    @Resource
    IPayGoodsParentService goodsParentService;
    /**
     * 生产支付参数
     * @return
     */
    @PostMapping("/create_paying_param")
    public RestResult<?> createPayingParam(@RequestBody GoodsPayParam param){
        return RestResult.success(goodsParentService.createPayingParam(param),"获取订单号成功");
    }

//    toCollection
    @PostMapping("/to_collection")
    public RestResult<?> toCollection(@RequestBody GoodsCollectionParam param){
        goodsParentService.toCollection(param);
        return RestResult.success("收藏成功","收藏成功");
    }

    @PostMapping("/cancel_collection")
    public RestResult<?> cancelCollection(@RequestBody GoodsCollectionParam param){
        goodsParentService.cancelCollection(param);
        return RestResult.success("收藏成功","收藏成功");

    }
//    /**
//     * 收藏参数
//     * @return
//     */
//    @PostMapping("/create_paying_param")
//    public RestResult<?> createPayingParam(@RequestBody GoodsParentParam param){
//        return RestResult.success(goodsParentService.createPayingParam(param),"获取订单号成功");
//    }

}
