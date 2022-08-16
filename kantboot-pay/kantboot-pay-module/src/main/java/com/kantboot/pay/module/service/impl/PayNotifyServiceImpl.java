package com.kantboot.pay.module.service.impl;

import com.alibaba.fastjson.JSON;
import com.kantboot.pay.module.entity.*;
import com.kantboot.pay.module.repository.*;
import com.kantboot.pay.module.service.IPayNotifyService;
import com.kantboot.pay.util.common.util.PayAfterResult;
import com.kantboot.system.user.module.entity.SysSetting;
import com.kantboot.system.user.module.security.TokenManage;
import com.kantboot.system.user.module.service.ISysSettingService;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.third.party.wechat.pay.entity.NotifyParam;
import com.kantboot.third.party.wechat.pay.entity.PayOrderQuery;
import com.kantboot.util.common.util.RestResult;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayNotifyServiceImpl implements IPayNotifyService {

    @Resource
    ISysSettingService sysSettingService;

    @Resource
    PayNotifyRepository repository;

    @Resource
    PayGoodsParentRepository payGoodsParentRepository;

    @Resource
    HttpServletRequest request;

    @Resource
    HttpServletResponse response;

    @Resource
    PayGoodsOrderRepository payGoodsOrderRepository;

    @Resource
    PayGoodsInOrderRepository payGoodsInOrderRepository;

    @Resource
    PayGoodsBuyRepository payGoodsRepository;

    @Resource
    TokenManage tokenManage;

    @Resource
    ISysUserService sysUserService;

    @Resource
    RestTemplate restTemplate;

    @Override
    public PayNotify getDefaultUse() {
        Long authPayNotifyId = sysSettingService.getSetting().getPayNotifyIdByDefault();
        PayNotify result = repository.findById(authPayNotifyId).get();
        return result;
    }

    @Override
    public PayNotify setDefaultUse(Long id) {
        SysSetting setting = sysSettingService.getSetting();
        setting.setPayNotifyIdByDefault(id);
        sysSettingService.save(setting);
        PayNotify result = repository.findById(id).get();
        return result;
    }

    /**
     * 确认支付后的回调请求
     */
    private void sentPayAfterUrl(PayGoodsInOrder payGoodsInOrder) {

        PayGoodsParent byName = payGoodsParentRepository.findByName(payGoodsInOrder.getPayGoodsParentName());
        // start:携带对应的响应头向支付前回调获取支付参数
        // 向商品获取支付后的回调，用来获取商品价格
        String payAfterUrl = byName.getPayAfterUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.add("token", tokenManage.getToken());
        headers.add("User-Agent", request.getHeader("User-Agent"));
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("goodsInOrderId",payGoodsInOrder.getId());
//        hashMap.put("totalFee",totalFee);
        HttpEntity<HashMap<String,Object>> requestEntity = new HttpEntity(hashMap, headers);
        System.out.println("payAfterUrl = " + payAfterUrl);
        RestResult<Object> forObject0 = restTemplate.postForObject(payAfterUrl, requestEntity, RestResult.class);
        String dataStr = JSON.toJSONString(forObject0.getData());
        PayAfterResult payAfterResult = JSON.parseObject(dataStr, PayAfterResult.class);

        if (payAfterResult.getIsSuccess()) {
            //如果消费端告诉成功，则停止继续发送
            System.out.println("购买成功！");
            return;
        }
        if(!payAfterResult.getIsSuccess()){
            //如果消费者端告诉不成功，则停止继续发送（后续再进行添加方法）
            return;
        }
    }

    @SneakyThrows
    @Override
    public Object payNotify() {

        //start:获取微信服务器传来的xml,并解析为微信的订单结果查询
        InputStream is = request.getInputStream();
        String string = is2String(is);
        is.close();
        NotifyParam notifyParam = NotifyParam.newInstance(string);
        // 订单查询结果
        PayOrderQuery payOrderQuery = notifyParam.createPayOrderQuery();

        // 查询订单
        String attach = payOrderQuery.getAttach();
        Map map = JSON.parseObject(attach, Map.class);
        Long id = Long.valueOf(map.get("id") + "");
        PayGoodsOrder cesAuthPayGoodsOrder = payGoodsOrderRepository.findById(id).get();

        Long totalFee = payOrderQuery.getTotalFee();
        // 如果金额对等
        if (payOrderQuery.getTotalFee().equals(cesAuthPayGoodsOrder.getTotalFee())) {

            //start:将状态转换成购买状态
            cesAuthPayGoodsOrder.setStatus(1);
            payGoodsOrderRepository.save(cesAuthPayGoodsOrder);
            //end:将状态转换成购买状态

            // 获取所有订单中的所有子订单
            List<PayGoodsInOrder> payGoodsInOrders = cesAuthPayGoodsOrder.getPayGoodsInOrders();
//            List<PayGoodsInOrder> payGoodsInOrders1 = new ArrayList<>();

            // 便利订单中的所有子订单
            for (PayGoodsInOrder payGoodsInOrder : payGoodsInOrders) {
                // 将订单中的所有子订单都转换为购买状态
                payGoodsInOrder.setStatus(1);
                payGoodsInOrderRepository.save(payGoodsInOrder);
//                payGoodsInOrders1.add(payGoodsInOrder);
                this.sentPayAfterUrl(payGoodsInOrder);
                //start:根据商品id、用户id和商品统一名称查询用户的该商品的仓库
                PayGoodsBuy byGoodsIdAndUserIdAndPayGoodsParentName = payGoodsRepository.findByGoodsIdAndUserIdAndPayGoodsParentName(
                        payGoodsInOrder.getGoodsId(),
                        payGoodsInOrder.getUserIdByBuy(),
                        payGoodsInOrder.getPayGoodsParentName()
                );

                //如果用户中该商品数量不为空
                if (byGoodsIdAndUserIdAndPayGoodsParentName != null) {

                    // 用户仓库中已购买的该商品数量
                    Long goodsNumber = byGoodsIdAndUserIdAndPayGoodsParentName.getNumber();

                    // start:将该商品的数量设置为“用户仓库中已购买的该商品数量”加上子订单中的数量
                    byGoodsIdAndUserIdAndPayGoodsParentName.setNumber(
                            goodsNumber + 1l
                    );
                    payGoodsRepository.save(byGoodsIdAndUserIdAndPayGoodsParentName);
                    // end:将该商品的数量设置为“用户仓库中已购买的该商品数量”加上子订单中的数量
                } else {
                    // start:如果用户的已购买的商品仓库中不存在该商品，则添加上该商品
                    PayGoodsBuy payGoodsBuy = new PayGoodsBuy().setGoodsId(payGoodsInOrder.getGoodsId())
                            .setUserId(payGoodsInOrder.getUserIdByBuy())
                            .setPayGoodsParentName(payGoodsInOrder.getPayGoodsParentName())
                            .setNumber(1l);
                    payGoodsRepository.save(payGoodsBuy);
                    // end:如果用户的已购买的商品仓库中不存在该商品，则添加上该商品
                }

            }

            System.out.println("JSON.toJSONString(payGoodsInOrders) = " + JSON.toJSONString(payGoodsInOrders));
            //添加所有商品的子订单
//            payGoodsInOrderRepository.saveAll(payGoodsInOrders);


        }

        // 告诉腾讯服务器已接收到不需要再发送
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<xml><return_msg>OK</return_msg><return_code>SUCCESS</return_code></xml>";
    }

    // 带编码的
    @SneakyThrows
    public String is2String(InputStream in) {
        StringBuffer out = new StringBuffer();
        InputStreamReader inread = new InputStreamReader(in, "UTF-8");
        char[] b = new char[4096];
        for (int n; (n = inread.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}
