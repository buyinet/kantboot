package com.kantboot.pay.util.common.controller;

import com.alibaba.fastjson.JSON;
import com.kantboot.pay.module.entity.PayGoodsBuy;
import com.kantboot.pay.module.entity.PayGoodsCollection;
import com.kantboot.pay.module.repository.PayGoodsCollectionRepository;
import com.kantboot.pay.module.repository.PayGoodsBuyRepository;
import com.kantboot.pay.util.common.annotation.GoodsEntityAnnotation;
import com.kantboot.pay.util.common.entity.BaseGoodsEntity;
import com.kantboot.system.user.module.entity.SysUser;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.util.common.util.RestResult;
import com.kantboot.util.core.controller.BaseController;
import com.kantboot.util.core.entity.CommonEntity;
import com.kantboot.util.core.entity.CommonEntityPageParam;
import com.kantboot.util.core.entity.OperatorEntity;
import com.kantboot.util.core.util.FindCommonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.LinkedList;
import java.util.List;

@RestController
@Slf4j
public class BaseGoodsController<T extends BaseGoodsEntity, ID> extends BaseController<T, ID> {

    @Resource
    ISysUserService userService;

    @Resource
    PayGoodsBuyRepository payGoodsRepository;
    @Resource
    PayGoodsCollectionRepository payGoodsCollectionRepository;
    @Resource
    FindCommonUtil<T> findCommonUtil;

    @PostMapping("/find_common_page_by_user_self_buy")
    public RestResult<?> findCommonPageByUserSelffBuy(@RequestBody CommonEntityPageParam<T> pageParam) {
        SysUser userInfo = userService.getUserInfo();

        PayGoodsBuy payGoodsBuy = new PayGoodsBuy().setUserId(userInfo.getId());
//        Set<PayGoodsInOrder> payGoodsInOrders=new HashSet<>();
//        payGoodsInOrders.add(new PayGoodsInOrder().setUserIdByBuy(userInfo.getId()).setStatus(1));
        CommonEntity<T> data = pageParam.getData();
        List<T> eq = data.getAnd().getEq();
        T entity = data.getEntity();
        entity.setPayGoodsBuy(payGoodsBuy);
//        entity.setPayGoodsInOrders(payGoodsInOrders);
        eq.add(entity);
        OperatorEntity<T> tOperatorEntity = data.getAnd().setEq(eq);
        data.setAnd(tOperatorEntity);
        return super.findCommonByPage(pageParam);
    }

    @PostMapping("/find_common_page_by_user_self_collection")
    public RestResult<?> findCommonPageByUserSelfCollection(@RequestBody CommonEntityPageParam<T> pageParam) {
        SysUser userInfo = userService.getUserInfo();

        PayGoodsCollection payGoopayGoodsCollections = new PayGoodsCollection().setUserId(userInfo.getId());
        CommonEntity<T> data = pageParam.getData();

        List<T> eq = data.getAnd().getEq();
        T entity = data.getEntity();
        entity.setPayGoodsCollection(payGoopayGoodsCollections);
        eq.add(entity);
        OperatorEntity<T> tOperatorEntity = data.getAnd().setEq(eq);
        data.setAnd(tOperatorEntity);
        RestResult<Page<T>> commonByPage = super.findCommonByPage(pageParam);
        List<T> content = commonByPage.getData().getContent();
        for (int i = 0; i < content.size(); i++) {
            T datum = content.get(i);
            RestResult<T> byId = findById(datum);
            content.get(i).setBuy( byId.getData().getBuy());
            content.get(i).setCollection( byId.getData().getCollection());
        }


        return commonByPage;
    }

    @PostMapping("/find_by_id")
    public RestResult<T> findById(@RequestBody T entity) {
        Long userId = userService.getUserInfo().getId();
        RestResult<T> byId = super.findById(entity);
        T data = byId.getData();
        PayGoodsBuy payGoods = payGoodsRepository.findByGoodsIdAndUserIdAndPayGoodsParentName(findCommonUtil.getId(entity) + "", userId,
                getParentName(data));
        PayGoodsCollection payGoodsCollection = payGoodsCollectionRepository.findByGoodsIdAndUserIdAndPayGoodsParentName(
                findCommonUtil.getId(entity) + "",
                userId,
                getParentName(data));
        System.out.println(JSON.toJSONString(payGoods));
        if (payGoods == null) {
            data.setBuy(false);
        } else if (payGoods != null) {
            data.setBuy(true);
        }

        if (payGoodsCollection == null) {
            data.setCollection(false);
        } else if (payGoodsCollection != null) {
            data.setCollection(true);
        }

        return RestResult.success(data, "获取成功");
    }


    @PostMapping("/find_common_page_by_pay_success")
    public RestResult<?> findCommonPageByPaySuccess(@RequestBody CommonEntity<T> commonEntity) {
        return null;
    }

    @Resource
    EntityManagerFactory entityManagerFactory;

    @PostMapping("/find_common_goods_list")
    public RestResult<List<T>> findCommonGoodsByList(@RequestBody CommonEntity<T> commonEntity) {
        RestResult<List<T>> commonByList = super.findCommonByList(commonEntity);
        List<T> data = commonByList.getData();
        List<T> result = new LinkedList<>();
        for (T datum : data) {
            RestResult<T> byId = findById(datum);
            result.add(byId.getData());
        }
        return RestResult.success(result, "获取成功");
    }

    @PostMapping("/find_common_goods_page")
    public RestResult<Page<T>> findCommonGoodsByPage(@RequestBody CommonEntityPageParam<T> pageParam) {
        RestResult<Page<T>> commonByPage = super.findCommonByPage(pageParam);
        List<T> content = commonByPage.getData().getContent();
        for (int i = 0; i < content.size(); i++) {
            T datum = content.get(i);
            RestResult<T> byId = findById(datum);
            content.get(i).setBuy( byId.getData().getBuy());
            content.get(i).setCollection( byId.getData().getCollection());
        }
        return commonByPage;
    }

    /**
     * 获取商品的统一名称
     *
     * @param object
     * @return
     */
    @SneakyThrows
    private String getParentName(Object object) {
        Class<?> aClass1 = Class.forName(object.getClass().getName());
        GoodsEntityAnnotation annotation = aClass1.getAnnotation(GoodsEntityAnnotation.class);
        if (annotation == null) {
            return null;
        }
        return annotation.parentName();
    }
}
