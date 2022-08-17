package com.kantboot.file.module.entity;

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

@Entity
@Table(name = "kfm_file_parent")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class KfmFileParent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "body_name")
    private String bodyName;

    @Column(name = "body_field")
    private String bodyField;

    /**
     * 存储类型：
     * 如：oss，path（本地存储）
     */
    @Column(name = "storage_type")
    private String storageType;

    /**
     * 文件类型
     */
    @Column(name = "file_type")
    private String fileType;

    @Column(name = "content")
    private String content;

    @Column(name = "file_oss_id")
    private Long fileOssId;

    @Column(name = "file_path_id")
    private Long filePathId;

    /**
     * 是否开启授权访问
     */
    @Column(name = "is_authorize_visit",columnDefinition="0")
    private Boolean authorizeVisit;

    @Column(name = "authorize_visit_notify_url")
    private String authorizeVisitCallbackUrl;
    @JsonIgnore
    @Column(name = "setting_id", columnDefinition = "1")
    private Long settingId;

    @JsonIgnore
    @OneToOne(targetEntity = SysSetting.class)
    @JoinColumn(name = "setting_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysSetting setting;

    /**
     * 是否使用水印
     */
    @Column(name = "is_use_watermark",columnDefinition="0")
    private Boolean useWatermark;


    /**
     * 水印对应的文件
     */
    @Column(name = "file_id_by_watermark")
    private String fileIdByWatermark;

    /**
     * 水印对应的url
     */
    @org.springframework.data.annotation.Transient
    private String fileUrlByWatermark;

    public String getFileUrlByWatermark() {
        if(getSetting()==null||getSetting().getFileVisitUrl()==null){
            return null;
        }
        return getSetting().getFileVisitUrl()+getFileIdByWatermark();
    }

    @OneToOne(targetEntity = KfmFileOss.class)
    @JoinColumn(name = "file_oss_id", referencedColumnName = "id", insertable = false, updatable = false)
    private KfmFileOss fileOss;

    @OneToOne(targetEntity = KfmFilePath.class)
    @JoinColumn(name = "file_path_id", referencedColumnName = "id", insertable = false, updatable = false)
    private KfmFilePath filePath;

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

}
