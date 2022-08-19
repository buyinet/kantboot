package com.kantboot.system.client.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单实体
 */
@Setter
@Getter
public class SysMenu  implements Serializable {

    /**
     * 编号
     */
    private Long id;


    /**
     * 路径
     */
    private String path;

    /**
     * 名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否为目录
     */
    private Boolean folder;

    /**
     * 菜单名称
     */
    private String label;

    /**
     * 页面路径
     */
    private String pagePath;

    /**
     * 优先级
     */
    private Long priority;

    /**
     * 父id
     */
    private Long pid;

    /**
     * 是否显示
     */
    private Boolean show;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    private Date gmtModified;

    private List<SysRole> roles;


    private List<SysMenu> children;

}
