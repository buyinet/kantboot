package com.kantboot.pay.util.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.kantboot.pay.module.entity.PayGoodsBuy;
import com.kantboot.pay.module.entity.PayGoodsCollection;
import com.kantboot.pay.module.repository.PayGoodsCollectionRepository;
import com.kantboot.pay.module.repository.PayGoodsBuyRepository;
import com.kantboot.pay.util.common.annotation.GoodsEntityAnnotation;
import com.kantboot.pay.util.common.entity.BaseGoodsEntity;
import com.kantboot.pay.util.common.service.IBaseGoodsService;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.util.core.service.impl.BaseServiceImpl;
import com.kantboot.util.core.util.FindCommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BaseGoodsServiceImpl<T extends BaseGoodsEntity,ID>
        extends BaseServiceImpl<T,ID>
        implements IBaseGoodsService<T,ID> {

    @Resource
    ISysUserService userService;
    @Resource
    PayGoodsBuyRepository payGoodsRepository;
    @Resource
    FindCommonUtil<T> findCommonUtil;
    @Resource
    PayGoodsCollectionRepository payGoodsCollectionRepository;

    @Override
    public T findById(T entity) {
        Long id = userService.getUserInfo().getId();
        T data = super.findById(entity);
        PayGoodsBuy payGoods = payGoodsRepository.findByGoodsIdAndUserIdAndPayGoodsParentName(findCommonUtil.getId(entity) + "", id, getParentName(data));
        System.out.println(JSON.toJSONString(payGoods));
        if (payGoods == null) {
            data.setBuy(false);
        }
        if (payGoods != null) {
            data.setBuy(true);
        }
        PayGoodsCollection payGoodsCollection = payGoodsCollectionRepository.findByGoodsIdAndUserIdAndPayGoodsParentName(findCommonUtil.getId(entity) + "", id,
                getParentName(data));
        if(payGoodsCollection==null){
            data.setCollection(false);
        }else if(payGoodsCollection!=null){
            data.setCollection(true);
        }

        return data;
    }

    public String getParentName(Object object) {
        GoodsEntityAnnotation annotation = object.getClass().getAnnotation(GoodsEntityAnnotation.class);
        if(annotation==null){
            return null;
        }
        return annotation.parentName();
    }
}
