package com.kantboot.file.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * oss管理
 */
@Entity
@Table(name = "kfm_file_oss")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class KfmFileOss {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    /**
     * 主目录
     */
    @Column(name = "body_folder")
    private String bodyFolder;

    @Column(name = "endpoint")
    private String endpoint;

    /**
     * 阿里云账号AccessKey拥有所有API的访问权限，风险很高。
     * 强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
     */
    @Column(name = "access_key_id")
    private String accessKeyId;

    @Column(name = "access_key_secret")
    private String accessKeySecret;

    @Column(name = "bucket_name")
    private String bucketName;

    @Column(name = "host")
    private String host;

    @Column(name = "callback_url")
    private String callbackUrl;

    @Column(name = "content")
    private String content;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gmt_create", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gmt_modified", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;
}
