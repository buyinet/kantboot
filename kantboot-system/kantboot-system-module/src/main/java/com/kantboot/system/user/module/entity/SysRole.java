package com.kantboot.system.user.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
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
 * 角色表
 */
@Entity
@Table(name = "sys_role")
@Getter
@Setter
@Accessors(chain = true)
@DynamicInsert(true)
@DynamicUpdate(true)
public class SysRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 角色名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 角色说明
     */
    @Column(name = "content")
    private String content;

    /**
     * 角色中文名称
     */
    @Column(name = "zh_name")
    private String zhName;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gmt_create", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    @ApiModelProperty(value = "最后一次修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gmt_modified", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;

    /**
     * 获得权限对应的所有权限
     */
    @ManyToMany(
            targetEntity = SysPermission.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "rel_role_permission",
            joinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "permission_id", referencedColumnName = "id")
            }
    )
    private List<SysPermission> permissions;

    //    /**
//     * 获得角色对应的所有用户
//     */
//    @ManyToMany(
//            targetEntity = SysUser.class,
//            cascade = CascadeType.DETACH,
//            fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "sys_user_role",
//            joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")}
//    )
//    @JsonIgnore
//    private Set<SysUser> user=new HashSet<>();
    @JsonIgnore
    @Column(name = "setting_id", columnDefinition = "1")
    private Long settingId;

    @JsonIgnore
    @OneToOne(targetEntity = SysSetting.class)
    @JoinColumn(name = "setting_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysSetting setting;

    @org.springframework.data.annotation.Transient
    private Boolean defaultUse;

    /**
     * 判断是否为默认的角色
     * @return
     */
    public Boolean getDefaultUse() {

        if (getSetting() == null) {
            return false;
        }
        if (getSetting().getRoleIdByDefaultUse().equals(id)) {
            return true;
        }
        return false;
    }


}
