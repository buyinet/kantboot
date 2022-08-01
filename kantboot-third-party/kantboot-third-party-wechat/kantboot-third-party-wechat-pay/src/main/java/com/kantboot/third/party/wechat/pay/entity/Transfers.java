package com.kantboot.third.party.wechat.pay.entity;

import com.github.wxpay.sdk.WXPayUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Data
@Accessors(chain = true)
@XStreamAlias("xml")
public class Transfers {

    private static final String REQUEST_URL
            = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    /**
     * 与商户好关联应用（如微信公众号/小程序）的appid
     */
    @XStreamAlias("mch_appid")
    private String mchAppid;

    /**
     * 微信支付分配的商户号
     */
    @XStreamAlias("mchid")
    private String mchid;

    /**
     * 商户订单号，需保持唯一性（只能是字母或者数字，不能包含其他字符）
     */
    @XStreamAlias("partner_trade")
    private String partnerTrade =
            (UUID.randomUUID().toString().replace("-","")
            +System.currentTimeMillis()).substring(0,32);;

    /**
     * NO_CHECK：不校验真实姓名
     * FORCE_CHECK：强校验真实姓名
     */
    @XStreamAlias("check_name")
    private String checkName="NO_CHECK";

    /**
     * 收款用户真实姓名。
     * 如果check_name设置为FORCE_CHECK，则必填用户真实姓名
     * 如需电子回单，需要传入收款用户姓名
     */
    @XStreamAlias("re_user_name")
    private String reUserName;

    /**
     * 企业付款金额
     */
    @XStreamAlias("amount")
    private Long amount;

    /**
     * 随机字符串，不长于32为
     */
    @XStreamAlias("nonce_str")
    private String nonceStr = WXPayUtil.generateNonceStr();;

    /**
     * 企业付款备注
     */
    @XStreamAlias("desc")
    private String desc;

    @XStreamAlias("sign")
    private String sign;

    /**
     * 是否校验用户姓名选项
     * @param bool
     *  false: NO_CHECK：不校验真实姓名 [默认]
     *  true: FORCE_CHECK：强校验真实姓名
     * @return
     */
    public Transfers setIsCheckName(Boolean bool){
        checkName = bool?"FORCE_CHECK":"NO_CHECK";
        return this;
    }

    /**
     * 是否校验用户姓名选项
     * @return  bool
     *  false: NO_CHECK：不校验真实姓名 [默认]
     *  true: FORCE_CHECK：强校验真实姓名
     */
    public Boolean getIsCheckName(){
        return checkName.equals("FORCE_CHECK");
    }

    public boolean doTransfers(File file, String key){
        StringBuffer stringBuffer=new StringBuffer("amount="+amount);
        stringBuffer.append("&check_name"+checkName);
        stringBuffer.append("&desc"+desc);
        stringBuffer.append("&mch_appid="+mchAppid);
        stringBuffer.append("&mchid="+mchid);
        stringBuffer.append("&nonce_str="+nonceStr);
        stringBuffer.append("&partner_trade="+partnerTrade);

        //生成签名
        this.sign =
                DigestUtils
                        .md5DigestAsHex(
                                stringBuffer.toString()
                                        .getBytes(StandardCharsets.UTF_8)).toUpperCase();
//        String result = new HttpRequestHandler().handle();
        return false;
    }

}
