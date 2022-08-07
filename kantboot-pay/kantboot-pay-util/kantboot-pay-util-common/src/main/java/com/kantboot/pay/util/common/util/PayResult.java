package com.kantboot.pay.util.common.util;

import lombok.Data;

import java.util.List;

@Data
public class PayResult {

    /**
     * 交易状态：（-1 为未交易，0 为正在交易中，1 为交易完成）
     */
    private Integer state;

    private String msg;

    private String errMsg;

    private String body;


    private List<PayParam> payParams;

    /**
     * 为交易金额
     *
     */
    private Long totalFee;

}
