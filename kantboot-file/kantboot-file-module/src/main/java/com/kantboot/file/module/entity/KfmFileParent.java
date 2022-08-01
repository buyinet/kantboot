package com.kantboot.file.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Column(name = "is_use_auth_visit", columnDefinition = "0")
    private Boolean useAuthVisit;

    /**
     * 授权访问回调地址
     */
    @Column(name = "use_auth_visit_callback_url")
    private String authVisitCallbackUrl;

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
