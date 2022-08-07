package com.kantboot.pay.util.common.controller;

import com.alibaba.fastjson.JSON;
import com.kantboot.pay.module.entity.PayGoods;
import com.kantboot.pay.util.common.entity.BaseGoodsEntity;
import com.kantboot.system.user.module.entity.SysUser;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.util.common.util.RestResult;
import com.kantboot.util.core.controller.BaseController;
import com.kantboot.util.core.entity.CommonEntity;
import com.kantboot.util.core.entity.CommonEntityPageParam;
import com.kantboot.util.core.entity.OperatorEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class BaseGoodsController<T extends BaseGoodsEntity,ID> extends BaseController<T,ID> {

    @Resource
    ISysUserService userService;

    @PostMapping("/find_common_page_by_user_self_buy")
    public RestResult<?> findCommonPageByMine(@RequestBody CommonEntityPageParam<T> pageParam){
        SysUser userInfo = userService.getUserInfo();

        PayGoods payGoods=new PayGoods().setUserId(userInfo.getId());
        System.out.println("userInfo.getId()="+userInfo.getId());
//        Set<PayGoodsInOrder> payGoodsInOrders=new HashSet<>();
//        payGoodsInOrders.add(new PayGoodsInOrder().setUserIdByBuy(userInfo.getId()).setStatus(1));
        CommonEntity<T> data = pageParam.getData();
        List<T> eq = data.getAnd().getEq();
        T entity = data.getEntity();
        entity.setPayGoods(payGoods);
//        entity.setPayGoodsInOrders(payGoodsInOrders);
        eq.add(entity);
        OperatorEntity<T> tOperatorEntity = data.getAnd().setEq(eq);
        data.setAnd(tOperatorEntity);
        System.out.println(JSON.toJSONString(pageParam));
        return findCommonByPage(pageParam);
    }


    @PostMapping("/find_common_page_by_pay_success")
    public RestResult<?> findCommonPageByPaySuccess(@RequestBody CommonEntity<T> commonEntity){
        return null;
    }
}
