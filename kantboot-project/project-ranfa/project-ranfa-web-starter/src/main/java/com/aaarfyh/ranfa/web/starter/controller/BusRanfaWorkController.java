package com.aaarfyh.ranfa.web.starter.controller;

import com.aaarfyh.ranfa.module.entity.BusRanfaBrand;
import com.aaarfyh.ranfa.module.entity.BusRanfaWork;
import com.aaarfyh.ranfa.module.repository.BusRanfaWorkRepository;
import com.aaarfyh.ranfa.module.service.IBusRanfaWorkService;
import com.kantboot.pay.module.entity.PayGoodsInOrder;
import com.kantboot.pay.module.repository.PayGoodsInOrderRepository;
import com.kantboot.pay.util.common.controller.BaseGoodsController;
import com.kantboot.pay.util.common.util.GoodsPayParam;
import com.kantboot.pay.util.common.util.PayAfterResult;
import com.kantboot.pay.util.common.util.PayResult;
import com.kantboot.system.user.module.entity.SysUser;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.util.common.util.RestResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@RequestMapping("/ranfa_work")
@Slf4j
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

    @PostMapping("/brand_to_change")
    public RestResult<BusRanfaWork> brandToChange(@RequestParam("ranfaBrandId") Long ranfaBrandId) {
        return RestResult.success(service.brandToChange(ranfaBrandId), "获取成功");
    }
    @PostMapping("/technique_to_change")
    public RestResult<BusRanfaWork> techniquesToChange(@RequestParam("techniqueId") Long ranfaTechniqueId) {
        return RestResult.success(service.techniqueToChange(ranfaTechniqueId), "获取成功");
    }
//    techniquesToChange

    @PostMapping("/brand_by_change")
    public RestResult<BusRanfaBrand> brandByChange(){
        return RestResult.success(service.brandByChange(), "获取成功");
    }

    /**
     * 接收支付前的回调
     * @param param
     * @return
     */
    @PostMapping("/pay_before")
    public RestResult<PayResult> payBefore(@RequestBody GoodsPayParam param) {
        return RestResult.success(service.payBefore(param), "回调成功");
    }

    @Resource
    PayGoodsInOrderRepository payGoodsInOrderRepository;

    @Resource
    ISysUserService sysUserService;

    @Resource
    BusRanfaWorkRepository repository;

    /**
     * 支付完成后回调
     * @param goodsInOrderId
     * @return
     */
    @SneakyThrows
    @PostMapping("/pay_after")
    public RestResult<PayAfterResult> payAfterResult(@RequestParam("goodsInOrderId") Long goodsInOrderId){
        System.out.println("goodsInOrderId = " + goodsInOrderId);
        final PayGoodsInOrder payGoodsInOrder = payGoodsInOrderRepository.findById(goodsInOrderId).get();
        final boolean bool = payGoodsInOrder.getPayGoodsParentName().equals("ranfa") &&
                payGoodsInOrder.getStatus().equals(1);
        System.out.println(bool+"------");

        if (!bool) {
            log.info("等待3秒后重新执行");
            return RestResult.success(new PayAfterResult().setIsSuccess(false),"获取成功");
        }

        if (bool) {
            BusRanfaWork byId = repository.findById(Long.parseLong(payGoodsInOrder.getGoodsId())).get();
            Long userIdByUpload = byId.getUserIdByUpload();
            SysUser byId1 = sysUserService.findById(new SysUser().setId(userIdByUpload));
            System.out.println(byId1.getPhoneNumber());
            System.out.println("userIdByUpload=" + userIdByUpload);
            //获取百分之60的利润给上传者
            Long price = byId.getPrice();
            long l = new BigDecimal(price).multiply(new BigDecimal(0.6)).longValue();

            sysUserService.addBalance(byId1.getId(), l);
        }




//        System.out.println(goodsInOrderId);
        return RestResult.success(new PayAfterResult().setIsSuccess(true),"获取成功");
    }

}
