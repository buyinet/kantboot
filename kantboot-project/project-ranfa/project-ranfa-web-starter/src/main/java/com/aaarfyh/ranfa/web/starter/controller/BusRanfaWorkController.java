package com.aaarfyh.ranfa.web.starter.controller;

import com.aaarfyh.ranfa.module.entity.BusRanfaBrand;
import com.aaarfyh.ranfa.module.entity.BusRanfaTechnique;
import com.aaarfyh.ranfa.module.entity.BusRanfaWork;
import com.aaarfyh.ranfa.module.repository.BusRanfaWorkRepository;
import com.aaarfyh.ranfa.module.service.IBusRanfaWorkService;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
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
import org.springframework.data.annotation.Transient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/ranfa_work")
@Slf4j
public class BusRanfaWorkController extends BaseGoodsController<BusRanfaWork, Long> {

    @Resource
    IBusRanfaWorkService service;

    private Interner<String> intern= Interners.<String>newStrongInterner();

    @Override
    @RequestMapping("/save")
    public RestResult<?> save(@RequestBody BusRanfaWork entity) {
        if(entity.getId()==null){
            String timeStr = (new Date().getTime()-999999999)+"";
            synchronized (intern.intern(timeStr)){
                entity.setIden(timeStr);
            }
        }
        return super.save(entity);
    }

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
    public RestResult<BusRanfaWork> techniquesToChange(@RequestParam("ranfaTechniqueId") Long ranfaTechniqueId) {
        System.err.println(ranfaTechniqueId);
        return RestResult.success(service.techniqueToChange(ranfaTechniqueId), "获取成功");
    }
//    techniquesToChange

    @PostMapping("/brand_by_change")
    public RestResult<BusRanfaBrand> brandByChange(){
        return RestResult.success(service.brandByChange(), "获取成功");
    }
    @PostMapping("/technique_by_change")
    public RestResult<BusRanfaTechnique> technique_by_change(){
        return RestResult.success(service.techniqueByChange(), "获取成功");
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
    @Transient
    @PostMapping("/pay_after")
    public RestResult<PayAfterResult> payAfterResult(@RequestParam("goodsInOrderId") Long goodsInOrderId){
        System.out.println("goodsInOrderId = " + goodsInOrderId);
        PayGoodsInOrder payGoodsInOrder = payGoodsInOrderRepository.findById(goodsInOrderId).get();
        boolean bool = payGoodsInOrder.getPayGoodsParentName().equals("ranfa") &&
                payGoodsInOrder.getStatus().equals(1);

        Long id = payGoodsInOrder.getId();
        String s = id + "";
        //当碰到一样的订单id则锁住
        synchronized (intern.intern(s)){
            
        }
        System.out.println(bool+"------");

        if (!bool) {
            return RestResult.success(new PayAfterResult().setIsSuccess(false),"获取成功");
        }

        // 如果已经回调过，则到此为止
        if(payGoodsInOrder.getCallBackPayAfter()){
            return RestResult.success(new PayAfterResult().setIsSuccess(false),"获取成功");
        }

        if (bool) {
            payGoodsInOrder.setCallBackPayAfter(true);
            payGoodsInOrderRepository.save(payGoodsInOrder);

            BusRanfaWork byId = repository.findById(Long.parseLong(payGoodsInOrder.getGoodsId())).get();
            Long userIdByUpload = byId.getUserIdByUpload();
            SysUser byId1 = sysUserService.findById(new SysUser().setId(userIdByUpload));
            System.out.println(byId1.getPhoneNumber());
            System.out.println("userIdByUpload=" + userIdByUpload);
            //获取百分之60的利润给上传者
            Long price = byId.getPrice();
            System.out.println(price+"-----------------------==");
            long l = new BigDecimal(price).multiply(new BigDecimal(0.6)).longValue()+1;
            sysUserService.addBalance(byId1.getId(), l);
        }




//        System.out.println(goodsInOrderId);
        return RestResult.success(new PayAfterResult().setIsSuccess(true),"获取成功");
    }

}
