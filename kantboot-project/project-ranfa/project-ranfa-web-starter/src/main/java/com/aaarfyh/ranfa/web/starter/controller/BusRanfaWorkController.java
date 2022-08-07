package com.aaarfyh.ranfa.web.starter.controller;

import com.aaarfyh.ranfa.module.entity.BusRanfaWork;
import com.aaarfyh.ranfa.module.service.IBusRanfaWorkService;
import com.kantboot.pay.util.common.controller.BaseGoodsController;
import com.kantboot.pay.util.common.util.GoodsParentParam;
import com.kantboot.pay.util.common.util.PayResult;
import com.kantboot.util.common.util.RestResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ranfa_work")
public class BusRanfaWorkController extends BaseGoodsController<BusRanfaWork, Long> {

    @Resource
    IBusRanfaWorkService service;


    /**
     * 切换作品
     * 随机推送
     */
    @PostMapping("/change")
    public RestResult<BusRanfaWork> change() {
        return RestResult.success(service.change(), "获取成功");
    }

    /**
     * 接收支付前的回调
     * @param param
     * @return
     */
    @PostMapping("/pay_before")
    public RestResult<PayResult> payBefore(@RequestBody GoodsParentParam param) {
        return RestResult.success(service.payBefore(param), "回调成功");
    }

}
