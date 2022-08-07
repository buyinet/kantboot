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
import java.util.List;

/**
 * 关于"组织"的实体类
 */
@Entity
@Table(name="sys_dept")
@ApiModel(value="SysDept")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class SysDept implements Serializable {

    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
     private Long id;

    /**
     * 父id
     */
    @Column(name="pid")
     private Long pid;

    /**
     * 层次
     */
    @Column(name="lv")
    private Integer lv;

    /**
     * 组织名称
     */
    @Column(name="name")
     private String name;


    /**
     * 组织介绍
     */
    @Column(name="content")
    private String content;

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


    @OneToMany(targetEntity = SysDept.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "pid",referencedColumnName = "id")
    private List<SysDept> children;

}
