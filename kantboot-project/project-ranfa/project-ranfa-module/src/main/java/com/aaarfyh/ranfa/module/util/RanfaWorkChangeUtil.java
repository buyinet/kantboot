package com.aaarfyh.ranfa.module.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RanfaWorkChangeUtil {

    /**
     * 用户可切换的区域数组
     * 当此区域全部切换后，会进入下一轮数组
     */
    private List<Long> blockIds;


    /**
     * 用户正在停留的下表
     */
    private Integer index;

    /**
     * 用户正在停留的id
     */
    private Long id;

    /**
     * 品牌id
     */
    private Long ranfaBrandId;

    /**
     * 分类id
     */
    private Long ranfaTechniqueId;

    private Integer page;

}
