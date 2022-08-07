package com.kantboot.system.user.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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

@Entity
@Table(name="rel_user_role")
@Getter
@Setter
@Accessors(chain = true)
@DynamicInsert(true)
@DynamicUpdate(true)
@EqualsAndHashCode
public class RelUserRole implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @Column(name="role_id")
    private Long roleId;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="gmt_modified",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;

    /**
     * 查看关联的用户
     */
    @OneToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "id",referencedColumnName = "user_id")
    private SysUser user;

    /**
     * 查看关联的角色
     */
    @OneToOne(targetEntity = SysRole.class)
    @JoinColumn(name = "id",referencedColumnName = "role_id")
    private SysUser role;
}
