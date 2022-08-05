package com.kantboot.system.user.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name="sys_setting")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
@NoArgsConstructor
public class SysSetting {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
//    /**
//     * 用户注册时的角色id
//     */
//    @Column(name="role_id_by_user_join")
//    private Long roleIdByUserJoin;

    /**
     * 默认角色id
     * 用例：
     *  在用户注册时会对应此角色用来注册
     */
    @Column(name="role_id_by_default_use")
    private Long roleIdByDefaultUse;

    /**
     * 采用默认的微信小程序Id
     */
    @Column(name="applet_wechat_param_id_by_default")
    private Long appletWechatParamIdByDefault;


    @Column(name="auth_pay_notify_id")
    private Long authPayNotifyId;

    @Column(name="host")
    private String host;

}
