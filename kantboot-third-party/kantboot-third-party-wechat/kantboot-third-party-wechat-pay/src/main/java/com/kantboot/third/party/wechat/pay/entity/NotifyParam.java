package com.kantboot.third.party.wechat.pay.entity;

import com.kantboot.util.common.util.WebSentUtil;
import com.kantboot.third.party.wechat.pay.config.XStreamBean;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *  通过回调得到的参数
 */
@Data
@Accessors(chain = true)
@XStreamAlias("xml")
public class NotifyParam {

    @XStreamAlias("appid")
    private String appid;

    @XStreamAlias("attach")
    private String attach;

    @XStreamAlias("bank_type")
    private String bankType;

    @XStreamAlias("cash_fee")
    private String cashFee;

    @XStreamAlias("fee_type")
    private String feeType;

    @XStreamAlias("is_subscribe")
    private String isSubscribe;

    @XStreamAlias("mch_id")
    private String mchId;

    @XStreamAlias("nonce_str")
    private String nonceStr;

    @XStreamAlias("openid")
    private String openid;

    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    @XStreamAlias("result_code")
    private String resultCode;

    @XStreamAlias("return_code")
    private String returnCode;

    @XStreamAlias("sign")
    private String sign;

    @XStreamAlias("time_end")
    private String timeEnd;

    @XStreamAlias("total_fee")
    private String totalFee;

    @XStreamAlias("trade_type")
    private String tradeType;

    @XStreamAlias("transaction_id")
    private String transactionId;

    public static final String ORDER_QUERY_URL="https://api.mch.weixin.qq.com/pay/orderquery";

    public static NotifyParam newInstance(String xmlStr) {
        XStream xStream = new XStreamBean().xStream();
        xStream.alias("xml",NotifyParam.class);
        NotifyParam notifyParam=(NotifyParam) xStream.fromXML(xmlStr);
        return notifyParam;
    }

    /**
     * 生成订单查询
     * @return
     */
    public PayOrderQuery createPayOrderQuery(){

        XStream xStream = new XStreamBean().xStream();
        String xmlStr= xStream.toXML(this);

        String resultStr = WebSentUtil.sendPost(ORDER_QUERY_URL, xmlStr);
        XStream xStream1=new XStreamBean().xStream();
        xStream1.alias("xml",PayOrderQuery.class);
        PayOrderQuery payOrderQuery = (PayOrderQuery) xStream1.fromXML(resultStr);
        return payOrderQuery;
    }


}
