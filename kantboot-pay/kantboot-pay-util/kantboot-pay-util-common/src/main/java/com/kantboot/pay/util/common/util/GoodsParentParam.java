package com.kantboot.pay.util.common.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GoodsParentParam {

    private String goodsParentName;
    private String userId;

    private List<PayParam> payParams;

}
