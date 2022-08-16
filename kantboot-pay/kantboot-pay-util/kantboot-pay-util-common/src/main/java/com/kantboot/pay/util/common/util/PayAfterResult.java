package com.kantboot.pay.util.common.util;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品支付后的回调
 */
@Data
@Accessors(chain = true)
public class PayAfterResult {
    /**
     * 是否支付成功
     */
    private Boolean isSuccess;
    /**
     * 如果为支付不成功，返回的消息字符串
     */
    private String noSuccessMsg;

}
