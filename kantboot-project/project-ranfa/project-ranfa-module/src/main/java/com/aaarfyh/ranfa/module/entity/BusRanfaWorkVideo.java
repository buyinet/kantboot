package com.aaarfyh.ranfa.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kantboot.system.user.module.entity.SysSetting;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bus_ranfa_work_video")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
public class BusRanfaWorkVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 染发作品id
     */
    @Column(name = "ranfa_work_id")
    private Long ranfaWorkId;

    @JsonIgnore
    @OneToOne(targetEntity = BusRanfaWork.class)
    @JoinColumn(name = "ranfa_work_id", referencedColumnName = "id", insertable = false, updatable = false)
    private BusRanfaWork ranfaWork;

    /**
     * 染发视频的文件id
     */
    @Column(name = "file_id_of_video")
    private Long fileIdOfVideo;

    /**
     * 集数
     */
    @Column(name = "episode")
    private Integer episode;

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

    @JsonIgnore
    @Column(name = "setting_id", columnDefinition = "1")
    private Long settingId;

    @JsonIgnore
    @OneToOne(targetEntity = SysSetting.class)
    @JoinColumn(name = "setting_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysSetting setting;

    /**
     * 染发视频的文件url
     *
     * @return
     */
    public String getFileUrlOfVideo() {
        return getSetting().getFileVisitUrl() + fileIdOfVideo;
    }

}
