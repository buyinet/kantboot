package com.aaarfyh.ranfa.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="rel_ranfa_work_technique")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class RelRanfaWorkTechnique {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="ranfa_work_id")
    private Long ranfaWorkId;

    @Column(name="ranfa_technique_id")
    private Long ranfaTechniqueId;

    /**
     * 创建时间
     */
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="gmt_create")
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="gmt_modified")
    private Date gmtModified;
}
