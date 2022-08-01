package com.kantboot.third.party.wechat.pay.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

/**
 * 付款到零钱
 * https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_1
 */
@Data
@Accessors(chain = true)
@XStreamAlias("xml")
@Component
public class PaymentToUserSmallChangeResult {
    @XStreamAlias("return_code")
    private String returnCode;
    @XStreamAlias("return_msg")
    private String returnMsg;
    @XStreamAlias("mch_appid")
    private String mchAppid;
    @XStreamAlias("mchid")
    private String mchid;
    @XStreamAlias("device_info")
    private String deviceInfo;
    @XStreamAlias("nonce_str")
    private String nonceStr;
    @XStreamAlias("result_code")
    private String resultCode;
    @XStreamAlias("partner_trade_no")
    private String partnerTradeNo;
    @XStreamAlias("payment_no")
    private String paymentNo;
    @XStreamAlias("payment_time")
    private String paymentTime;
    @XStreamAlias("err_code")
    private String errCode;
    @XStreamAlias("err_code_des")
    private String errCodeDes;

    /**
     * 是否提现成功
     * @return
     */
    public Boolean isSuccess(){
        if(resultCode.equals("SUCCESS")){
            return true;
        }
        return false;
    }


}
