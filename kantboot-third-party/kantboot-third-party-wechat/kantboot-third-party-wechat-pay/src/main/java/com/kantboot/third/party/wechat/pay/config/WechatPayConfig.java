package com.kantboot.third.party.wechat.pay.config;


import com.kantboot.third.party.wechat.pay.entity.PayUnifiedOrder;
import com.kantboot.third.party.wechat.pay.entity.PayingParam;
import com.kantboot.third.party.wechat.pay.entity.PaymentToUserSmallChange;
import com.kantboot.third.party.wechat.pay.entity.PaymentToUserSmallChangeResult;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 微信支付主配置类
 */
@Component
@Data
@Lazy
@Accessors(chain = true)
public class WechatPayConfig {

    @Value("${wechat.applet.appid}")
    private String appid;

    @Value("${wechat.applet.secret}")
    private String secret;

    @Value("${wechat.pay.mch-id}")
    private String mchId;

    @Value("${wechat.pay.mch-key}")
    private String mchKey;

    @Value("${wechat.pay.notify-uri}")
    private String notifyUri;

    @Value("${wechat.pay.cert-file-path}")
    private String certFilePath;

    /**
     * 生成传给前端支付的参数
     * @return
     */
    public PayingParam createPayingParam(PayUnifiedOrder payUnifiedOrder){

        payUnifiedOrder
                .setAppid(this.appid)
                .setMchId(this.mchId);

        if(payUnifiedOrder.getNotifyUrl()==null||
                payUnifiedOrder.getNotifyUrl().equals("")){
            payUnifiedOrder
                    .setNotifyUrl(notifyUri);
        }

        PayingParam payingParam =
                payUnifiedOrder.createPayingParam(mchKey);

        return payingParam;
    }

    /**
     * 生成付款到零钱结果的参数
     */
    public PaymentToUserSmallChangeResult
    createPaymentToUserSmallChangeResult(PaymentToUserSmallChange paymentToUserSmallChange){

        PaymentToUserSmallChangeResult paymentToUserSmallChangeResult
                = paymentToUserSmallChange
                .setMchAppid(this.appid)
                .setMchid(this.mchId)
                .setCertFilePath(this.certFilePath)
                .createSign(mchKey)
                .createPaymentToUserSmallChangeResult();

        return paymentToUserSmallChangeResult;
    }



}
