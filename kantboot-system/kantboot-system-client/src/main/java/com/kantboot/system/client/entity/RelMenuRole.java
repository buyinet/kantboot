package com.kantboot.system.client.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 关于权限的
 */
@Getter
@Setter
@Accessors(chain = true)
public class RelMenuRole implements Serializable {

    private Long id;

    private Long menuId;

    private Long roleId;

    private Date gmtCreate;

    private Date gmtModified;

}
