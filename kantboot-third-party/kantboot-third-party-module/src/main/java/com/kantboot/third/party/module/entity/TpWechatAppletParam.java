package com.kantboot.third.party.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kantboot.system.user.entity.SysSetting;
import com.kantboot.third.party.wechat.applet.config.WechatAppletConfig;
import com.kantboot.third.party.wechat.pay.config.WechatPayConfig;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 微信小程序参数
 */
@Entity
@Table(name="tp_wechat_applet_param")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class TpWechatAppletParam {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 标识
     */
    @Column(name="name")
    private String name;

    /**
     * 标题
     */
    @Column(name="title")
    private String title;

    @Column(name="app_id")
    private String appId;

    @Column(name="app_secret")
    private String appSecret;

    /**
     * 描述
     */
    @Column(name="content")
    private String content;

    /**
     * 微信支付id
     */
    @Column(name="pay_wechat_pay_id")
    private Long payWechatPayId;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="gmt_create",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="gmt_modified",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;

    @OneToOne(targetEntity = TpWechatPayParam.class,
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "pay_wechat_pay_id",referencedColumnName = "id",insertable = false,updatable = false)
    private TpWechatPayParam payWechatPay;

    /**
     * 获取WechatAppletConfig类
     * 用于方便业务需求，如登录业务
     * @return
     */
    public WechatAppletConfig getWechatAppletConfig(){
        if(payWechatPayId==null){
            return null;
        }
        return new WechatAppletConfig().setSecret(appSecret).setAppid(appId);
    }

    public WechatPayConfig getWechatPayConfig(){
        if(payWechatPayId==null){
            return null;
        }
        return new WechatPayConfig()
                .setAppid(appId)
                .setSecret(appSecret)
                .setNotifyUri(payWechatPay.getNotifyUri())
                .setMchKey(payWechatPay.getMchKey())
                .setMchId(payWechatPay.getMchId());
    }

    @JsonIgnore
    @Column(name="setting_id",columnDefinition="1")
    private Long settingId;

    @JsonIgnore
    @OneToOne(targetEntity = SysSetting.class)
    @JoinColumn(name = "setting_id",referencedColumnName = "id",insertable = false,updatable = false)
    private SysSetting setting;

    private Boolean isDefault(){
        if(getSetting()==null){
            return false;
        }
        if(getSetting().getAppletWechatParamIdByDefault().equals(id)){
            return true;
        }
        return false;
    }

}
