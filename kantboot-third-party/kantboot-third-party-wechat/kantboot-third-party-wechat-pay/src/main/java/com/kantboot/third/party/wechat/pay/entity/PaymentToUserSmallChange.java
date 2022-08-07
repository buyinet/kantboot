package com.kantboot.third.party.wechat.pay.entity;

import com.github.wxpay.sdk.WXPayUtil;
import com.kantboot.util.common.util.WebSentUtil;
import com.kantboot.third.party.wechat.pay.config.XStreamBean;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 付款到零钱
 * https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_1
 */
@Data
@Accessors(chain = true)
@XStreamAlias("xml")
@Component
public class PaymentToUserSmallChange {

    private static final String URL="https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    /**
     * 证书路径
     */
    private String certFilePath;

    @XStreamAlias("mch_appid")
    private String mchAppid;

    @XStreamAlias("mchid")
    private String mchid;

    @XStreamAlias("nonce_str")
    private String nonceStr= WXPayUtil.generateNonceStr();;

    @XStreamAlias("partner_trade_no")
    private String partnerTradeNo=UUID.randomUUID().toString().replaceAll("-","");

    @XStreamAlias("openid")
    private String openid;

    /**
     * 校验姓名
     * NO_CHECK：不校验真实姓名
     * FORCE_CHECK：强校验真实姓名
     */
    @XStreamAlias("check_name")
    private String checkName;

    @XStreamAlias("re_user_name")
    private String reUserName;

    @XStreamAlias("amount")
    private Integer amount;

    @XStreamAlias("desc")
    private String desc;

    @XStreamAlias("spbill_create_ip")
    private String spbillCreateIp;

    @XStreamAlias("sign")
    private String sign;

    public PaymentToUserSmallChange setIsCheckName(Boolean b){
        if(b){
            this.checkName="FORCE_CHECK";
            return this;
        }
        this.checkName="NO_CHECK";
        return this;
    }

    /**
     * 生成签名
     *
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
     * @return
     */
    @SneakyThrows
    public PaymentToUserSmallChange createSign(String mchKey){

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("amount=" + this.amount);

        if(this.checkName!=null&&!(this.checkName.equals(""))){
            stringBuffer.append("&check_name="+this.checkName);
        }

        if(this.desc!=null&&!(this.desc.equals(""))){
            stringBuffer.append("&desc="+this.desc);
        }

        if (this.mchAppid!=null&&!(this.mchAppid.equals(""))){
            stringBuffer.append("&mch_appid=" + this.mchAppid);
        }

        if (this.mchid!=null&&!(this.mchid.equals(""))){
            stringBuffer.append("&mchid=" + this.mchid);
        }

        if(this.nonceStr!=null&&!(this.nonceStr.equals("")))
            stringBuffer.append("&nonce_str=" + this.nonceStr);

        if(this.openid!=null && !(this.openid.equals("")))
            stringBuffer.append("&openid="+ this.openid);

        if(this.partnerTradeNo!=null&& !(this.partnerTradeNo.equals("")))
            stringBuffer.append("&partner_trade_no="+this.partnerTradeNo);

        if(this.reUserName!=null && !(this.reUserName.equals("")))
            stringBuffer.append("&re_user_name="+this.reUserName);

        if(this.spbillCreateIp!=null&&!(this.spbillCreateIp.equals("")))
            stringBuffer.append("&spbill_create_ip="+this.spbillCreateIp);

        stringBuffer.append("&key="+mchKey);


        this.sign =
                DigestUtils
                        .md5DigestAsHex(
                                stringBuffer.toString()
                                        .getBytes(StandardCharsets.UTF_8)).toUpperCase();

        return this;
    }

    @SneakyThrows
    public PaymentToUserSmallChangeResult createPaymentToUserSmallChangeResult(){
        XStream xStream=new XStreamBean().xStream();
        String s = xStream.toXML(this);
        String s1 = WebSentUtil.httpClientSSL(s,
                this.mchid,
                PaymentToUserSmallChange.URL,
                this.certFilePath
                );
        xStream.alias("xml",PaymentToUserSmallChangeResult.class);
        PaymentToUserSmallChangeResult o = (PaymentToUserSmallChangeResult) xStream.fromXML(s1);
        return o;
    }

//    @SneakyThrows
//    public static void main(String[] args) {
//        PaymentToUserSmallChange w=new PaymentToUserSmallChange()
//                .setMchid("1566114621")
//                .setMchAppid("wxcfaee02a530f4c2f")
//                .setAmount(30)
//                .setPartnerTradeNo(UUID.randomUUID().toString().replaceAll("-",""))
//                .setDesc("测试")
//                .setOpenid("o3hsE5abC69H9RyvBUkWrnCFBYAg")
//                .setCheckName("NO_CHECK")
//                .setSpbillCreateIp("127.0.0.1")
//                .createSign("Wsfzy123123123123123123123123123");
////        w.toPay();
//        XStream xStream=new XStreamBean().xStream();
//        String s = xStream.toXML(w);
//        System.out.println(s);
//        String s1 = WebSentUtil.httpClientSSL(s, "1566114621",
//                PaymentToUserSmallChange.URL, "/Users/kantboot/cert/1566114621_20220119_cert/apiclient_cert.p12");
//        System.out.println(s1);
//        xStream.alias("xml",PaymentToUserSmallChangeResult.class);
//        PaymentToUserSmallChangeResult o = (PaymentToUserSmallChangeResult) xStream.fromXML(s1);
//        System.out.println(o);
//    }

}
