package com.kantboot.pay.module.entity;

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
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="pay_notify")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class PayNotify implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     */
    @Column(name="title")
    private String title;

    /**
     * 编号
     */
    private String iden;

    /**
     * 回调地址
     */
    @Column(name="url")
    private String url;

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
        if (getSetting().getPayNotifyIdByDefault().equals(getId())) {
            return true;
        }
        return false;
    }
}
