package com.kantboot.third.party.wechat.pay.entity;

import com.github.wxpay.sdk.WXPayUtil;
import com.kantboot.project.util.common.util.WebSentUtil;
import com.kantboot.third.party.wechat.pay.config.XStreamBean;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * 下单接口
 */
@Data
@Accessors(chain = true)
@XStreamAlias("xml")
public class PayUnifiedOrder {

    /**
     * 微信支付统一下单回调url
     */
    public final static String UNIFIEDORDER_URL
            = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 公众账号ID
     *
     * 微信支付分配的公众账号ID（企业号corpid即为此appid）
     */
    @XStreamAlias("appid")
    private String appid;

    /**
     * 商户号
     *
     * 微信支付分配的商户号
     */
    @XStreamAlias("mch_id")
    private String mchId;

    /**
     * 随机字符串
     *
     * 随机字符串，长度要求在32位以内。
     * 推荐<a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3">随机数生成算法</a>
     */
    @XStreamAlias("nonce_str")
    private String nonceStr = WXPayUtil.generateNonceStr();

    /**
     * 签名
     *
     * 建议使用随机生成签名
     */
    @XStreamAlias("sign")
    private String sign;

    /**
     * 商品描述
     *
     * 商品简单描述，
     * 该字段请按照规范传递，
     * 具体请见<a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2">参数规定</a>
     */
    @XStreamAlias("body")
    private String body;

    /**
     * 附加数据
     *
     * 在查询API和支付通知中原样返回，
     * 可作为自定义参数使用。
     */
    @XStreamAlias("attach")
    private String attach;

    /**
     * 交易类型
     *
     * JSAPI -JSAPI支付
     * NATIVE -Native支付
     * APP -APP支付
     */
    @XStreamAlias("trade_type")
    private String tradeType = "JSAPI";

    /**
     * 商品id
     *
     * trade_type=NATIVE时，
     * 此参数必传。
     * 此参数为二维码中包含的商品ID，
     * 商户自行定义。
     */
    @XStreamAlias("product_id")
    private String productId;

    /**
     * 商户订单号
     *
     * 商户系统内部订单号，
     * 要求32个字符内，
     * 只能是数字、
     * 大小写字母_-|*
     * 且在同一个商户号下唯一。详见商户订单号
     */
    @XStreamAlias("out_trade_no")
    private String outTradeNo =
            (UUID.randomUUID().toString().replace("-","")
                    +System.currentTimeMillis()).substring(0,32);

    /**
     * 标价金额
     *
     * 订单总金额，单位为分，
     * 详见<a href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2">支付金额</a>
     */
    @XStreamAlias("total_fee")
    private Long totalFee=1l;

    /**
     * 用户ip
     */
    @XStreamAlias("spbill_create_ip")
    private String spbillCreateIp;


    /**
     * 通知地址
     *
     * body 异步接收微信支付结果通知的回调地址，
     * 通知url必须为外网可访问的url，
     * 不能携带参数。
     *
     * 公网域名必须为https，
     * 如果是走专线接入，
     * 使用专线NAT IP或者私有回调域名可使用http
     *
     */
    @XStreamAlias("notify_url")
    private String notifyUrl;

    /**
     * 用户标识
     *
     *trade_type=JSAPI时（即JSAPI支付），
     * 此参数必传，
     * 此参数为微信用户在商户对应appid下的唯一标识。
     *
     */
    @XStreamAlias("openid")
    private String openid;

    @XStreamAlias("device_info")
    private String deviceInfo;

    /**
     * 生成签名
     *
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
     * @return
     */
    @SneakyThrows
    private PayUnifiedOrder createSign(String mchKey){

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("appid=" + this.appid);

        if(this.attach!=null&&!(this.attach.equals("")))
            stringBuffer.append("&attach="+this.attach);

        if(this.body!=null&&!(this.body.equals(""))){
            stringBuffer.append("&body="+this.body);
        }

        if (this.mchId!=null&&!(this.mchId.equals("")))
            stringBuffer.append("&mch_id=" + this.mchId);

        if(this.nonceStr!=null&&!(this.nonceStr.equals("")))
            stringBuffer.append("&nonce_str=" + this.nonceStr);

        if(this.notifyUrl!=null && !(this.notifyUrl.equals("")))
            stringBuffer.append("&notify_url="+ this.notifyUrl);

        if(this.openid!=null&& !(this.openid.equals("")))
            stringBuffer.append("&openid="+this.openid);

        if(this.outTradeNo!=null&& !(this.outTradeNo.equals("")))
            stringBuffer.append("&out_trade_no="+this.outTradeNo);

        if(this.spbillCreateIp!=null && !(this.spbillCreateIp.equals("")))
            stringBuffer.append("&spbill_create_ip="+this.spbillCreateIp);

        if(this.totalFee!=null&&this.totalFee>=0)
            stringBuffer.append("&total_fee="+this.totalFee);

        if(this.tradeType!=null&&!(this.tradeType.equals("")))
            stringBuffer.append("&trade_type="+this.tradeType);

        stringBuffer.append("&key="+mchKey);

        this.sign =
                DigestUtils
                        .md5DigestAsHex(
                                stringBuffer.toString()
                                        .getBytes(StandardCharsets.UTF_8)).toUpperCase();
        return this;
    }


    /**
     * 生成前端付款时需要的参数
     *
     * @return 前端付款时需要的参数
     */
    @SneakyThrows
    public PayingParam createPayingParam(String mchKey){
        //首先生成sign
        this.createSign(mchKey);


        // 发送请求获取 prepay_id
        XStream xStream=new XStreamBean().xStream();
        String xml=xStream.toXML(this);
        String xmlStr = WebSentUtil.sendPost(UNIFIEDORDER_URL, xml);
        System.out.println(xmlStr);
        String prepay_id = "";// 预支付id
        if (xmlStr.indexOf("SUCCESS") != -1) {
            Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
            prepay_id = map.get("prepay_id");

        }

        PayingParam payingParam=new PayingParam();
        payingParam.setAppId(this.appid);
        payingParam.setNonceStr(this.nonceStr);
        payingParam.setPackage1("prepay_id=" + prepay_id);

        String params="appId="+payingParam.getAppId()+
                "&nonceStr="+payingParam.getNonceStr()+
                "&package="+payingParam.getPackage1()+
                "&signType="+payingParam.getSignType()+
                "&timeStamp="+payingParam.getTimeStamp()+
                "&key="+mchKey;

        payingParam.setPaySign(DigestUtils.md5DigestAsHex(params.getBytes(StandardCharsets.UTF_8)));
        return payingParam;
    }

}



