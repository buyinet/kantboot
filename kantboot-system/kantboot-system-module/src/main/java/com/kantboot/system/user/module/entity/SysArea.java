package com.kantboot.system.user.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 地区管理
 */
@Entity
@Table(name="sys_area")
@ApiModel(value="SysArea")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class SysArea implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value="名称")
    @Column(name="name")
    private String name;

    /**
     * 地区编号
     */
    @ApiModelProperty(value="地区编号")
    @Column(name="iden")
    private String iden;

    /**
     * 父级编码
     */
    @ApiModelProperty(value="父级编码")
    @Column(name="parent_iden")
    private String parentIden;


    @ApiModelProperty(value="lv0编码")
    @Column(name="lv0_iden")
    private String lv0Iden;

    @ApiModelProperty(value="lv1编码")
    @Column(name="lv1_iden")
    private String lv1Iden;

    /**
     * 层次（0 省份/直辖市，1 市/直辖市的区县，3 区县）
     */
    @ApiModelProperty(value="层次（0 省份/直辖市，1 市/直辖市的区县，3 区县）")
    @Column(name="lv")
    private Integer lv;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="gmt_create",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    @ApiModelProperty(value="最后一次修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="gmt_modified",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;

    /**
     * 区域对应的父级区域
     */
    @ApiModelProperty(value = "对应的父级id")
    @ManyToOne(targetEntity = SysArea.class,cascade = CascadeType.DETACH)
    @JoinColumn(name = "parent_iden",referencedColumnName = "iden",insertable = false,updatable = false)
    private SysArea parentArea;


    private static final long serialVersionUID = 1L;
}