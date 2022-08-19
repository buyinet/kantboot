package com.kantboot.system.client.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
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


    @Column(name="pay_notify_id_by_default")
    private Long payNotifyIdByDefault;

    @Column(name="host")
    private String host;

    /**
     * 文件查看的url
     * 根据名称查看文件
     */
    @org.springframework.data.annotation.Transient
    private String fileVisitUrl;


     public String getFileVisitUrl(){
        return getHost()+"/kantboot-file/file/visit/";
    }

}
