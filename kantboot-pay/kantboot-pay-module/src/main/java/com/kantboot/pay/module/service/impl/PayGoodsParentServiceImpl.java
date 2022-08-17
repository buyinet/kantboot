package com.kantboot.pay.module.service.impl;

import com.alibaba.fastjson.JSON;
import com.kantboot.pay.module.entity.PayGoodsCollection;
import com.kantboot.pay.module.entity.PayGoodsInOrder;
import com.kantboot.pay.module.entity.PayGoodsOrder;
import com.kantboot.pay.module.entity.PayGoodsParent;
import com.kantboot.pay.module.repository.PayGoodsCollectionRepository;
import com.kantboot.pay.module.repository.PayGoodsInOrderRepository;
import com.kantboot.pay.module.repository.PayGoodsOrderRepository;
import com.kantboot.pay.module.repository.PayGoodsParentRepository;
import com.kantboot.pay.module.service.IPayGoodsParentService;
import com.kantboot.pay.module.service.IPayNotifyService;
import com.kantboot.pay.util.common.util.*;
import com.kantboot.system.user.module.entity.SysUser;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.third.party.module.service.ITpUserOfWechatService;
import com.kantboot.third.party.module.service.ITpWechatAppletParamService;
import com.kantboot.third.party.wechat.pay.entity.PayUnifiedOrder;
import com.kantboot.third.party.wechat.pay.entity.PayingParam;
import com.kantboot.util.common.util.RestResult;
import com.kantboot.util.core.service.impl.BaseServiceImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayGoodsParentServiceImpl
        extends BaseServiceImpl<PayGoodsParent,Long>
        implements IPayGoodsParentService {

    @Resource
    PayGoodsParentRepository repository;
    @Resource
    HttpServletRequest request;
    @Resource
    RestTemplate restTemplate;

    @Resource
    ISysUserService userService;

    @Resource
    ITpUserOfWechatService tpUserOfWechatService;

    @Resource
    ITpWechatAppletParamService tpWechatAppletParamService;

    @Resource
    PayGoodsOrderRepository payGoodsOrderRepository;

    @Resource
    PayGoodsInOrderRepository payGoodsInOrderRepository;

    @Resource
    IPayNotifyService payNotifyService;


    /**
     * 根据参数中的那么获取商品统一管理
     * @param param
     * @return
     */
    private PayGoodsParent getPayGoodsParent(GoodsPayParam param){
        String goodsParentName = param.getGoodsParentName();
        PayGoodsParent result = repository.findByName(goodsParentName);
        return result;
    }

    /**
     * 携带对应的响应头向支付前回调获取支付参数
     * 向支付前需要回调的url传参,获取对应的商品信息，以供生成支付参数
     * @return
     */
    private PayResult getPayResult(PayGoodsParent payGoodsParent,
                                   GoodsPayParam param){
        //start:携带对应的响应头向支付前回调获取支付参数
        String payBeforeUrl = payGoodsParent.getPayBeforeUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.add("token",request.getHeader("token"));
        headers.add("User-Agent",request.getHeader("User-Agent"));
        HttpEntity<GoodsPayParam> requestEntity = new HttpEntity(param, headers);
        RestResult restResult = restTemplate.postForObject(payBeforeUrl,requestEntity,RestResult.class);
        //end:携带对应的响应头向支付前回调获取支付参数
        Object data = restResult.getData();
        String payResultJsonStr = JSON.toJSONString(data);
        PayResult result = JSON.parseObject(payResultJsonStr,PayResult.class);
        return result;
    }

    /**
     * 添加订单信息
     */
    private PayGoodsOrder addPayGoodsOrder(PayResult payResult){
        SysUser userInfo = userService.getUserInfo();
        // start:新增订单
        PayGoodsOrder payGoodsOrder = new PayGoodsOrder();
        

        payGoodsOrder
                .setUserIdByBuy(userInfo.getId())
                .setUserIdByPay(userInfo.getId())//添加支付者id，为了扩展以后的代付功能
                .setStatus(0) //强制设置状态为未支付
                .setTotalFee(payResult.getTotalFee()); //设置总金额
        PayGoodsOrder result = payGoodsOrderRepository.save(payGoodsOrder);
        return result;

    };
    private Iterable<PayGoodsInOrder> addPayGoodsInOrder(PayResult payResult, PayGoodsOrder payGoodsOrder, PayGoodsParent payGoodsParent){
        ArrayList<PayGoodsInOrder> payGoodsInOrders = new ArrayList<>();
        List<PayParam> payParams = payResult.getPayParams();
        for (PayParam payParamItem : payParams) {
            String goodsId = payParamItem.getGoodsId();
            PayGoodsInOrder payGoodsInOrder = new PayGoodsInOrder();
            payGoodsInOrder.setPayGoodsOrderId(payGoodsOrder.getId());
            payGoodsInOrder.setGoodsId(goodsId);
            // 添加商品统一管理名称
            payGoodsInOrder.setPayGoodsParentName(payGoodsParent.getName());
            payGoodsInOrder.setTitle(payGoodsInOrder.getTitle());
            payGoodsInOrder.setStatus(0);
            payGoodsInOrder.setUserIdByBuy(payGoodsOrder.getUserIdByBuy());
            payGoodsInOrder.setUserIdByPay(payGoodsOrder.getUserIdByPay());
            payGoodsInOrders.add(payGoodsInOrder);
        }

        Iterable<PayGoodsInOrder> result = payGoodsInOrderRepository.saveAll(payGoodsInOrders);
        return result;
    }

    @Override
    public Object createPayingParam(GoodsPayParam param) {

        PayGoodsParent payGoodsParent = this.getPayGoodsParent(param);

        PayResult payResult = this.getPayResult(payGoodsParent, param);

        PayGoodsOrder payGoodsOrder = this.addPayGoodsOrder(payResult);

        Iterable<PayGoodsInOrder> payGoodsInOrders = this.addPayGoodsInOrder(payResult, payGoodsOrder, payGoodsParent);



        PayUnifiedOrder payUnifiedOrder=
                new PayUnifiedOrder()
                        .setAttach(JSON.toJSONString(payGoodsOrder.setPayGoodsInOrders(null)))
                        .setBody(payResult.getBody())
                        .setNotifyUrl(payNotifyService.getDefaultUse().getUrl())
                        .setOpenid(tpUserOfWechatService.getUserInfo().getOpenid())
//                        .setOpenid(tpUserOfWechatService.getUserInfo().getUnionid())
                        .setTotalFee(payResult.getTotalFee());
        PayingParam payingParam = tpWechatAppletParamService.getDefaultUse().getWechatPayConfig().createPayingParam(payUnifiedOrder);
        return payingParam;
    }

    @Resource
    PayGoodsCollectionRepository payGoodsCollectionRepository;
    @Override
    public Object toCollection(GoodsCollectionParam param) {

        Long userId = userService.getUserInfo().getId();

        List<PayGoodsCollection> payGoodsCollections=new ArrayList<>();
        List<CollectionParam> collectionParams = param.getCollectionParams();
        for (CollectionParam collectionParam : collectionParams) {
            String goodsId = collectionParam.getGoodsId();
            payGoodsCollections.add(
                    new PayGoodsCollection()
                            .setGoodsId(collectionParam.getGoodsId())
                            .setUserId(userId)
                            .setPayGoodsParentName(param.getGoodsParentName())
            );
        }

        payGoodsCollectionRepository.saveAll(payGoodsCollections);

        return null;
    }

    @Override
    public Object cancelCollection(GoodsCollectionParam param) {
        Long userId = userService.getUserInfo().getId();
        List<CollectionParam> collectionParams = param.getCollectionParams();
        List<PayGoodsCollection> payGoodsCollections=new ArrayList<>();
        for (CollectionParam collectionParam : collectionParams) {
            PayGoodsCollection byGoodsIdAndUserIdAndPayGoodsParentName = payGoodsCollectionRepository.findByGoodsIdAndUserIdAndPayGoodsParentName(
                    collectionParam.getGoodsId(),
                    userId,
                    param.getGoodsParentName());
            if(byGoodsIdAndUserIdAndPayGoodsParentName!=null){
                payGoodsCollections.add(byGoodsIdAndUserIdAndPayGoodsParentName);
            }

        }
        payGoodsCollectionRepository.deleteAll(payGoodsCollections);
        return null;
    }
}
