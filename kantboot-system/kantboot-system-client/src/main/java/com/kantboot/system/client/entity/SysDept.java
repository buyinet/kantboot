package com.kantboot.system.client.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 关于"组织"的实体类
 */
@Setter
@Getter
public class SysDept implements Serializable {

    /**
     * 编号
     */
     private Long id;

    /**
     * 父id
     */
     private Long pid;

    /**
     * 层次
     */
    private Integer lv;

    /**
     * 组织名称
     */
     private String name;


    /**
     * 组织介绍
     */
    private String content;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    private Date gmtModified;


    private List<SysDept> children;

}
