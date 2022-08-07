package com.kantboot.pay.web.starter.controller;

import com.kantboot.pay.module.entity.PayGoods;
import com.kantboot.pay.util.common.util.GoodsParentParam;
import com.kantboot.pay.util.common.util.PayResult;
import com.kantboot.system.user.module.entity.SysUser;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.util.common.util.RestResult;
import com.kantboot.util.core.controller.BaseController;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@Log4j2
@RequestMapping("/pay_goods")
public class PayGoodsController extends BaseController<PayGoods,Long> {


    @Resource
    ISysUserService userService;

    @PostMapping("/aa")
    public RestResult<PayResult> aa(@RequestBody GoodsParentParam param){
        SysUser userInfo = userService.getUserInfo();
        PayResult result=new PayResult();
        result.setState(2000);
        result.setTotalFee(1l);
        result.setBody("");
        return RestResult.success(result,"回调成功");
    }

    @GetMapping("/bb")
    public String bb(){
        return "221111122";
    }

    @GetMapping("/cc")
    public String cc(){
        return "2111233";
    }


//    /**
//     * 统一回调接口
//     * @param request
//     * @param response
//     * @return
//     */
//    @SneakyThrows
//    @RequestMapping("/notify")
//    public String notify(HttpServletRequest request, HttpServletResponse response){
//
//        //start:获取微信服务器传来的xml,并解析为微信的订单结果查询
//        InputStream is = request.getInputStream();
//        String string = is2String(is);
//        is.close();
//        NotifyParam notifyParam = NotifyParam.newInstance(string);
//        // 订单查询结果
//        PayOrderQuery payOrderQuery = notifyParam.createPayOrderQuery();
//        log.info("微信订单查询结果:\t"+payOrderQuery.toString());
//
//        // 查询订单
//        String attach = payOrderQuery.getAttach();
//        Map map = JSON.parseObject(attach, Map.class);
//        Long id = Long.valueOf(map.get("id")+"");
//        CesAuthPayGoodsOrder cesAuthPayGoodsOrder = authPayGoodsOrderRepository.findById(id).get();
//
//        // 如果金额对等则化为已购买状态
//        if(payOrderQuery.getTotalFee().equals(cesAuthPayGoodsOrder.getTotalFee())){
//            cesAuthPayGoodsOrder.setStatus(1);
//            authPayGoodsOrderRepository.save(cesAuthPayGoodsOrder);
//        }
//
////        if(payOrderQuery.getTotalFee().equals()){}
//        //end:获取微信服务器传来的xml,并解析为微信的订单结果查询
//
//
//
////        // start:携带对应的响应头向支付前回调获取支付参数
////        // 向商品获取支付后的回调，用来获取商品价格
////        String payBeforeUrl = byName.getPayBeforeUrl();
////
////        HttpHeaders headers = new HttpHeaders();
////        headers.add("token",request.getHeader("token"));
////        headers.add("User-Agent",request.getHeader("User-Agent"));
////        HttpEntity<GoodsParentParam> requestEntity = new HttpEntity(param, headers);
////        RestResult forObject0 = restTemplate.postForObject(payBeforeUrl,requestEntity,RestResult.class);
////        // end:携带对应的响应头向支付前回调获取支付参数
//
//        // 告诉腾讯服务器已接收到不需要再发送
//        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
//                "<xml><return_msg>OK</return_msg><return_code>SUCCESS</return_code></xml>";
//    }

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
