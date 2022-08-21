package com.kantboot.third.party.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kantboot.system.user.module.entity.SysSetting;
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
 * 微信支付参数
 */
@Entity
@Table(name="tp_wechat_pay_param")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class TpWechatPayParam {

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

    @Column(name="mch_id")
    private String mchId;

    @Column(name="mch_key")
    private String mchKey;

    /**
     * 回调地址
     */
    @Column(name="notify_uri")
    private String notifyUri;

    /**
     * 描述
     */
    @Column(name="content")
    private String content;

    /**
     * 微信证书id
     */
    @Column(name="file_id_by_apiclient_cert_p12")
    private Long fileIdByApiclientCertP12;

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
    @JsonIgnore
    @Column(name = "setting_id", columnDefinition = "1")
    private Long settingId;

    @JsonIgnore
    @OneToOne(targetEntity = SysSetting.class)
    @JoinColumn(name = "setting_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysSetting setting;

    @org.springframework.data.annotation.Transient
    private String fileUrlByApiclientCertP12;

    public String getFileUrlByApiclientCertP12(){
//        System.out.println("getFileIdByApiclientCertP12()11 = " + getSetting().getFileVisitUrl()+this.getFileIdByApiclientCertP12());
        if(getSetting()!=null){
            return  getSetting().getFileVisitUrl()+this.getFileIdByApiclientCertP12();
        }
        return null;
    }
}



