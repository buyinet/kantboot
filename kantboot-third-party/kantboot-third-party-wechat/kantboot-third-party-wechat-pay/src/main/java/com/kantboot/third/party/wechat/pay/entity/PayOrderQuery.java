package com.kantboot.third.party.wechat.pay.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 订单查询
 */
@Data
@Accessors(chain = true)
@XStreamAlias("xml")
public class PayOrderQuery implements Serializable {

    /**
     * SUCCESS/FAIL
     *
     * 此字段是通信标识
     * 非交易标识
     * 交易是否成功需要查看trade_state来判断
     */
    @XStreamAlias("return_code")
    private String returnCode;

    /**
     * 返回信息
     *
     * 当return_code为FAIL时返回信息为错误原因 ，例如
     * 签名失败
     * 参数格式校验错误
     */
    @XStreamAlias("return_msg")
    private String returnMsg;

    // 以下字段在 return_code 为 SUCCESS 的时候有返回

    // 以下字段在 return_code 为 SUCCESS 的时候有返回

    // 以下字段在 return_code 为 SUCCESS 的时候有返回


    /**
     * 公众账号ID
     *
     * 微信分配的公众账号ID
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
     * 随机字符串，不长于32位。推荐<a src="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3">随机数生成算法</a>
     */
    @XStreamAlias("nonce_str")
    private String nonceStr;

    /**
     * 签名
     *
     * 签名，详见<a src="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3">签名生成算法</a>
     */
    @XStreamAlias("sign")
    private String sign;

    /**
     * 业务结果
     *
     * SUCCESS/FAIL
     */
    @XStreamAlias("result_code")
    private String resultCode;

    /**
     * 错误代码
     *
     * 当result_code为FAIL时返回错误代码
     * 详细参见下文错误列表
     */
    @XStreamAlias("errCode")
    private String errCode;

    /**
     * 错误代码描述
     *
     * 当result_code为FAIL时返回错误描述
     * 详细参见下文错误列表
     */
    @XStreamAlias("errCodeDes")
    private String errCodeDes;

    /**
     * 设备号
     *
     * 微信支付分配的终端设备号
     */
    @XStreamAlias("device_info")
    private String deviceInfo;

    /**
     * 用户标识
     *
     * 用户在商户appid下的唯一标识
     */
    @XStreamAlias("openid")
    private String openid;

    /**
     * 是否关注公众账号
     *
     * 用户是否关注公众账号
     * Y-关注，N-未关注
     */
    @XStreamAlias("is_subscribe")
    private String isSubscribe;

    /**
     * 交易类型
     *
     *调用接口提交的交易类型，
     * 取值如下：JSAPI，NATIVE，APP，MICROPAY，
     * 详细说明见<a src="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2">参数规定</a>
     */
    @XStreamAlias("trade_type")
    private String tradeType;

    /**
     * 交易状态
     *
     * SUCCESS--支付成功
     * REFUND--转入退款
     * NOTPAY--未支付
     * CLOSED--已关闭
     * REVOKED--已撤销(刷卡支付)
     * USERPAYING--用户支付中
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     * ACCEPT--已接收，等待扣款
     * 支付状态机请见下单API页面
     */
    @XStreamAlias("trade_state")
    private String tradeState;

    /**
     * 付款银行
     *
     * 银行类型
     * 采用字符串类型的银行标识
     */
    @XStreamAlias("bank_type")
    private String bankType;

    /**
     * 标价金额
     *
     * 订单总金额
     * 单位为分
     */
    @XStreamAlias("total_fee")
    private Long totalFee;

    /**
     * 应结订单金额
     *
     * 当订单使用了免充值型优惠券后返回该参数，
     * 应结订单金额=订单金额-免充值优惠券金额
     */
    @XStreamAlias("settlement_total_fee")
    private String settlementTotalFee;

    /**
     * 货币类型
     * 符合ISO 4217标准的三位字母代码，
     * 默认人民币：CNY，其他值列表详见<a src="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2">货币类型</a>
     */
    @XStreamAlias("fee_type")
    private String feeType;

    /**
     * 现金支付金额
     *
     * 现金支付金额订单现金支付金额，
     * 详见<a src="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2">支付金额</a>
     */
    @XStreamAlias("cash_fee")
    private String cashFee;

    /**
     * 现金支付币种
     *
     * 货币类型，符合ISO 4217标准的三位字母代码，
     * 默认人民币：CNY，
     * 其他值列表详见<a src="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2">支付金额</a>
     */
    @XStreamAlias("cash_fee_type")
    private String cashFeeType;

    /**
     * 代金券金额
     *
     * “代金券”金额<=订单金额，
     * 订单金额-“代金券”金额=现金支付金额，
     * 详见<a src="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2">支付金额</a>
     */
    @XStreamAlias("coupon_fee")
    private String couponFee;


    /**
     * 微信支付订单号
     *
     * 微信支付订单号
     */
    @XStreamAlias("transaction_id")
    private String transactionId;

    /**
     * 商户订单号
     *
     * 商户系统内部订单号，
     * 要求32个字符内，
     * 只能是数字、
     * 大小写字母_-|*@ ，且在同一个商户号下唯
     */
    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    /**
     * 附加数据
     *
     * 附加数据，原样返回
     */
    @XStreamAlias("attach")
    private String attach;

    /**
     * 支付完成时间
     *
     * 订单支付时间，
     * 格式为yyyyMMddHHmmss，
     * 如2009年12月25日9点10分10秒表示为20091225091010。
     * 其他详见<a src="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2">时间规则</a>
     */
    @XStreamAlias("time_end")
    private String timeEnd;

    /**
     * 交易状态描述
     *
     * 对当前查询订单状态的描述和下一步操作的指引
     */
    @XStreamAlias("trade_state_desc")
    private String tradeStateDesc;

    /**
     * 是否返回成功
     */
    public Boolean isReturnSuccess(){
        if(returnCode!=null&&returnCode.equals("SUCCESS"))
            return true;
        return false;
    }

    /**
     * 查看业务结果
     * @return
     */
    public Boolean isResultSuccess(){
        if(resultCode!=null&&resultCode.equals("SUCCESS"))
            return true;
        return false;
    }

    /**
     * 是否完成付款
     * @return
     */
    public Boolean isPaySuccess(){
        if(isResultSuccess()&&isResultSuccess()&&tradeState!=null&&tradeState.equals("SUCCESS"))
            return true;
        return false;
    }

    /**
     * 用户是否关注公众号
     * @return
     */
    public Boolean isSubscribe(){
        if(isSubscribe!=null&&isSubscribe.equals("Y"))
            return true;
        return false;
    }



}
