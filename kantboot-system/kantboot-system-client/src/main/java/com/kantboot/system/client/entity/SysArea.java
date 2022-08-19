package com.kantboot.system.client.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
public class SysArea implements Serializable {

    private Long id;

    private String name;

    private String iden;

    private String parentIden;

    private String lv0Iden;

    private String lv1Iden;

    private Integer lv;

    private Date gmtCreate;

    private Date gmtModified;

    private SysArea parentArea;


    private static final long serialVersionUID = 1L;
}