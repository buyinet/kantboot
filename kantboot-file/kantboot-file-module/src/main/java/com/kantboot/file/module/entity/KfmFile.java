package com.kantboot.file.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kantboot.system.user.entity.SysSetting;
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
 * 文件仓库
 */
@Entity
@Table(name = "kfm_file")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class KfmFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "path")
    private String path;

    @Column(name = "name")
    private String name;


    @Column(name = "file_parent_id")
    private Long fileParentId;

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


    @OneToOne(targetEntity = KfmFileOss.class)
    @JoinColumn(name = "file_oss_id", referencedColumnName = "id", insertable = false, updatable = false)
    private KfmFileOss fileOss;

    @OneToOne(targetEntity = KfmFilePath.class)
    @JoinColumn(name = "file_path_id", referencedColumnName = "id", insertable = false, updatable = false)
    private KfmFilePath filePath;

    @OneToOne(targetEntity = KfmFileParent.class)
    @JoinColumn(name = "file_parent_id", referencedColumnName = "id", insertable = false, updatable = false)
    private KfmFileParent fileParent;
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

    @org.springframework.data.annotation.Transient
    @JsonIgnore
    @Column(name = "setting_id", columnDefinition = "1")
    private Long settingId;

    @org.springframework.data.annotation.Transient
    @JsonIgnore
    @OneToOne(targetEntity = SysSetting.class)
    @JoinColumn(name = "setting_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysSetting setting;

    public String getVisitUrlById() {
        if (getSetting() == null) {
            return null;
        }
        return getSetting().getHost() + "/kantboot-file/file/visit/" + id;
    }

    public String getVisitUrlByName() {
        if (getSetting() == null) {
            return null;
        }
        return getSetting().getHost() + "/kantboot-file/file/visit/" + this.fileParent.getBodyName() + "/" + this.fileParent.getBodyField() + "/" + name;
    }

}
