package com.kantboot.system.client.entity;

import java.io.Serializable;
import java.util.Date;

public class RelUserRole implements Serializable {


    private Long id;

    private Long userId;

    private Long roleId;

    private Date gmtCreate;

    private Date gmtModified;

    private SysUser user;

    private SysUser role;
}
