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
import org.springframework.cache.annotation.CacheConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户实体-SysUser
 * 用在用户的登录、注册、获取个人信息、
 * 获取角色、判断权限等增删查改的各方面
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@Table(name="sys_user")
@ApiModel(
        value="sysUser",
        description = "用在用户的登录、注册、" +
                "获取个人信息、获取角色、" +
                "判断权限等增删查改的各方面")
@CacheConfig(cacheNames = "user")
public class SysUser implements Serializable {

    @ApiModelProperty(value = "主键",name="主键(id)", required = true)
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @Column(name="username")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",example = "123456", required = true)
    @Column(name="password")
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称",example = "等灯瞪等灯")
    @Column(name="nickname")
    private String nickname;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    @Column(name="realname")
    private String realname;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    @Column(name="avatar_url")
    private String avatarUrl;


    /**
     * 性别
     */
    @ApiModelProperty(value = "性别（0 女、1 男）")
    @Column(name="gender")
    private Integer gender;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    @Column(name="phone_number")
    private String phoneNumber;

    /**
     * 身份证
     */
    @ApiModelProperty(value = "身份证")
    @Column(name="id_card")
    private String idCard;

    /**
     * 余额（以“分”为单位）
     */
    @ApiModelProperty(value = "余额")
    @Column(name="balance",columnDefinition="0")
    private Long balance;

    /**
     * 余额（以“元“为单位”）
     */
    @org.springframework.data.annotation.Transient
    private Double balanceYuan;

    /**
     * 余额，以元为单位
     * @param balanceYuan
     */
    public SysUser setBalanceYuan(Long balanceYuan) {
        BigDecimal bigDecimal = new BigDecimal(balanceYuan);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        this.setBalance(multiply.longValue());
        return this;
    }

    /**
     * 余额，以元为单位
     */
    public Double getBalanceYuan() {
        if(getBalance()==null){
            return null;
        }
        BigDecimal bigDecimal = new BigDecimal(getBalance());
        BigDecimal multiply = bigDecimal.divide(new BigDecimal(100));
        return multiply.doubleValue();
    }


    /**
     * 总收入
     */
    @ApiModelProperty(value = "总收入")
    @Column(name="total_revenue")
    private Long totalRevenue;

    /**
     * 创建时IP
     */
    @ApiModelProperty(value = "创建时IP")
    @Column(name="create_ip")
    private String createIp;

    /**
     * 创建时设备
     */
    @ApiModelProperty(value = "创建时设备或UA")
    @Column(name="create_device")
    private String createDevice;

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

    @Deprecated
    @ApiModelProperty(value = "是否激活")
    @Column(name="enabled")
    private Boolean enabled;

    @Deprecated
    @ApiModelProperty(value = "是否锁定")
    @Column(name="locked")
    private Boolean locked;

    @Column(name="year")
    private Long year;

    /**
     * 余额(以"元"为单位)
     */
//    public Double getBalanceYuan(){
//        return balance/100.0;
//    }


    /**
     * 用户对应的所有角色
     */
    @ApiModelProperty(value = "用户对应的所有角色")
    @ManyToMany(targetEntity = SysRole.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinTable(name = "rel_user_role",
            joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")}
    )
    private List<SysRole> roles;

    /**
     * 用户对应的所有组织
     */
    @ApiModelProperty(value = "用户对应的所有组织")
    @ManyToMany(targetEntity = SysDept.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinTable(name = "rel_user_dept",
            joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "dept_id",referencedColumnName = "id")}
    )
    private Set<SysDept> depts;

}
