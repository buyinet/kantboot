package com.kantboot.pay.util.common.util;

import lombok.Data;

import java.util.List;

@Data
public class GoodsCollectionParam {
    private String goodsParentName;
    private String userId;

    private List<CollectionParam> collectionParams;

}
