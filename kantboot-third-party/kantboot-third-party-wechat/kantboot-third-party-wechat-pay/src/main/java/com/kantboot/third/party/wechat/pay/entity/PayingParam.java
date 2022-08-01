package com.kantboot.third.party.wechat.pay.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.wxpay.sdk.WXPayUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 传给前端支付的参数
 */
@Data
@XStreamAlias("xml")
public class PayingParam {

    @XStreamAlias("appId")
    private String appId;

    @XStreamAlias("timeStamp")
    private String timeStamp = WXPayUtil.getCurrentTimestamp() + "";

    @XStreamAlias("nonceStr")
    private String nonceStr = WXPayUtil.generateNonceStr();

    @XStreamAlias("signType")
    private String signType = "MD5";

    @XStreamAlias("package")
    @JsonProperty("package")
    @JSONField(name = "package")
    private String package1;

    @XStreamAlias("paySign")
    private String paySign;


}
