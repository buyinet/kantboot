package com.kantboot.third.party.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kantboot.system.user.entity.SysUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 微信用户
 */
@Entity
@Table(name="tp_user_of_wechat")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class TpUseOfWechat {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private Long userId;

    /**
     * 微信公众平台提供用来识别的一种用户ID
     */
    @JsonIgnore
    @Column(name="openid")
    private String openid;

    /**
     * 微信开放平台提供用来识别的一种用户ID
     */
    @JsonIgnore
    @Column(name="unionid")
    private String unionid;

    /**
     * 昵称
     */
    @Column(name="nickname")
    private String nickname;

    /**
     * 性别(0 女，1 男)
     */
    @Column(name="gender")
    private Integer gender;

    /**
     * 城市
     */
    @Column(name="city")
    private String city;

    /**
     * 省份
     */
    @Column(name="province")
    private String province;

    /**
     * 国家
     */
    @Column(name="country")
    private String country;

    /**
     * 头像
     */
    @Column(name="avatar_url")
    private String avatarUrl;

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
     * 查看关联的用户实体
     */
    @OneToOne(targetEntity = SysUser.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id",insertable = false,updatable = false)
    private SysUser user;
}
