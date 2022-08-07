package com.kantboot.pay.web.starter.controller;

import com.alibaba.fastjson.JSON;
import com.kantboot.pay.module.entity.PayGoods;
import com.kantboot.pay.module.entity.PayGoodsInOrder;
import com.kantboot.pay.module.entity.PayGoodsOrder;
import com.kantboot.pay.module.entity.PayNotify;
import com.kantboot.pay.module.repository.PayGoodsInOrderRepository;
import com.kantboot.pay.module.repository.PayGoodsOrderRepository;
import com.kantboot.pay.module.repository.PayGoodsRepository;
import com.kantboot.pay.module.service.IPayNotifyService;
import com.kantboot.third.party.wechat.pay.entity.NotifyParam;
import com.kantboot.third.party.wechat.pay.entity.PayOrderQuery;
import com.kantboot.util.common.util.RestResult;
import com.kantboot.util.core.controller.BaseController;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/pay_notify")
public class PayNotifyController extends BaseController<PayNotify,Long> {

    @Resource
    IPayNotifyService service;
    @Resource
    PayGoodsOrderRepository payGoodsOrderRepository;

    @Resource
    PayGoodsInOrderRepository payGoodsInOrderRepository;

    @Resource
    PayGoodsRepository payGoodsRepository;

    @PostMapping("/set_default_use")
    public RestResult<?> setDefaultUse(@RequestParam("id") Long id){
        return RestResult.success(service.setDefaultUse(id),"设置成功");
    }

    @PostMapping("/get_default_use")
    public RestResult<?> getDefaultUse(){
        return RestResult.success(service.getDefaultUse(),"获取成功");
    }

    /**
     * 统一回调接口
     * @param request
     * @param response
     * @return
     */
    @SneakyThrows
    @RequestMapping("/notify")
    public String notify(HttpServletRequest request, HttpServletResponse response){

        //start:获取微信服务器传来的xml,并解析为微信的订单结果查询
        InputStream is = request.getInputStream();
        String string = is2String(is);
        is.close();
        NotifyParam notifyParam = NotifyParam.newInstance(string);
        // 订单查询结果
        PayOrderQuery payOrderQuery = notifyParam.createPayOrderQuery();
        log.info("微信订单查询结果:\t"+payOrderQuery.toString());

        // 查询订单
        String attach = payOrderQuery.getAttach();
        Map map = JSON.parseObject(attach, Map.class);
        Long id = Long.valueOf(map.get("id")+"");
        PayGoodsOrder cesAuthPayGoodsOrder = payGoodsOrderRepository.findById(id).get();

        System.out.println("payOrderQuery.getTotalFee().equals(cesAuthPayGoodsOrder.getTotalFee()) = " + payOrderQuery.getTotalFee().equals(cesAuthPayGoodsOrder.getTotalFee()));
        System.out.println("cesAuthPayGoodsOrder.getTotalFee() = " + cesAuthPayGoodsOrder.getTotalFee());
        System.out.println("payOrderQuery.getTotalFee() = " + payOrderQuery.getTotalFee());
        // 如果金额对等则化为已购买状态
        if(payOrderQuery.getTotalFee().equals(cesAuthPayGoodsOrder.getTotalFee())){
            cesAuthPayGoodsOrder.setStatus(1);
            System.out.println("===========");
            payGoodsOrderRepository.save(cesAuthPayGoodsOrder);
            List<PayGoodsInOrder> payGoodsInOrders = cesAuthPayGoodsOrder.getPayGoodsInOrders();
            for (PayGoodsInOrder payGoodsInOrder : payGoodsInOrders) {
                payGoodsInOrder.setStatus(1);
                PayGoods byGoodsIdAndUserIdAndPayGoodsParentName = payGoodsRepository.findByGoodsIdAndUserIdAndPayGoodsParentName(
                        payGoodsInOrder.getGoodsId(),
                        payGoodsInOrder.getUserIdByBuy(),
                        payGoodsInOrder.getPayGoodsParentName()
                );

                    if(byGoodsIdAndUserIdAndPayGoodsParentName!=null){
                        byGoodsIdAndUserIdAndPayGoodsParentName.setNumber(
                                byGoodsIdAndUserIdAndPayGoodsParentName.getNumber()+1
                        );
                        payGoodsRepository.save(byGoodsIdAndUserIdAndPayGoodsParentName);
                    }else{
                        PayGoods payGoods = new PayGoods().setGoodsId(payGoodsInOrder.getGoodsId())
                                .setUserId(payGoodsInOrder.getUserIdByBuy())
                                .setPayGoodsParentName(payGoodsInOrder.getPayGoodsParentName())
                                .setNumber(1l)
                                ;
                        payGoodsRepository.save(payGoods);


                    }

            }
            payGoodsInOrderRepository.saveAll(payGoodsInOrders);
        }


//        if(payOrderQuery.getTotalFee().equals()){}
        //end:获取微信服务器传来的xml,并解析为微信的订单结果查询



//        // start:携带对应的响应头向支付前回调获取支付参数
//        // 向商品获取支付后的回调，用来获取商品价格
//        String payBeforeUrl = byName.getPayBeforeUrl();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("token",request.getHeader("token"));
//        headers.add("User-Agent",request.getHeader("User-Agent"));
//        HttpEntity<GoodsParentParam> requestEntity = new HttpEntity(param, headers);
//        RestResult forObject0 = restTemplate.postForObject(payBeforeUrl,requestEntity,RestResult.class);
//        // end:携带对应的响应头向支付前回调获取支付参数

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
        for (int n; (n = inread.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}
