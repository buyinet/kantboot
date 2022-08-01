package com.kantboot.system.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.HashSet;
import java.util.Set;

/**
 * 权限表
 */
@Entity
@Table(name="sys_permission")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class SysPermission implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="zh_name")
    private String zhName;

    @Column(name="uri")
    private String uri;

    @Column(name="is_matcher")
    private Boolean matcher;

    @Column(name="pid")
    private Long pid;

    /**
     * 描述
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

    @ApiModelProperty(value = "全员通行")
    @Column(name="is_all_person_current")
    private Boolean allPersonCurrent;

    /**
     * 获得角色对应的所有权限
     */
    @ManyToMany(
            targetEntity = SysUser.class,
            cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER)
    @JoinTable(
            foreignKey = @ForeignKey(name="none",value= ConstraintMode.NO_CONSTRAINT),
            name = "rel_role_permission",
            joinColumns = {
                    @JoinColumn(name = "permission_id",referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id",referencedColumnName = "id")
            }
    )
    @JsonIgnore
    private Set<SysRole> user=new HashSet<>();

    @Override
    public String toString() {
        return "SysPermission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", zhName='" + zhName + '\'' +
                ", uri='" + uri + '\'' +
                ", matcher=" + matcher +
                ", pid=" + pid +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", allPersonCurrent=" + allPersonCurrent +
                '}';
    }
}
