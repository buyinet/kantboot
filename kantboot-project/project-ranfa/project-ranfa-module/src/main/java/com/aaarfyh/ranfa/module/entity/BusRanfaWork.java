package com.aaarfyh.ranfa.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kantboot.pay.module.entity.PayGoodsBuy;
import com.kantboot.pay.module.entity.PayGoodsCollection;
import com.kantboot.pay.module.entity.PayGoodsInOrder;
import com.kantboot.pay.util.common.annotation.GoodsEntityAnnotation;
import com.kantboot.pay.util.common.entity.BaseGoodsEntity;
import com.kantboot.system.user.module.entity.SysSetting;
import com.kantboot.system.user.module.entity.SysUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * 染发作品
 */
@Entity
@Table(name="bus_ranfa_work")
@GoodsEntityAnnotation(parentName = "ranfa")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class BusRanfaWork implements BaseGoodsEntity,Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="user_id_by_upload")
    private Long userIdByUpload;

    @Column(name="title")
    private String title;

    @Column(name="content")
    private String content;

    @Column(name="ranfa_brand_id")
    private Long ranfaBrandId;

    @OneToOne(targetEntity = BusRanfaBrand.class)
    @JoinColumn(name = "ranfa_brand_id",referencedColumnName = "id",insertable = false,updatable = false)
    private BusRanfaBrand ranfaBrand;

    @Column(name="ranfa_technique_id")
    private Long ranfaTechniqueId;

    @Column(name="price")
    private Long price;

    /**
     * 染发前封面的文件id
     */
    @Column(name="file_id_by_front_cover_image")
    private Long fileIdByFrontCoverImage;


    /**
     * 染发后封面的文件id
     */
    @Column(name="file_id_by_back_cover_image")
    private Long fileIdByBackCoverImage;

    /**
     * 审核状态
     */
    @Column(name="audit_status",columnDefinition="0")
    private Integer auditStatus;

    /**
     * 审核不通过原因
     */
    @Column(name="reasons_for_failure_in_audit")
    private String reasonsForFailureInAudit;

    /**
     * 过程
     */
    @Column(name="process")
    private String process;


    @ManyToMany(
            targetEntity = BusRanfaTechnique.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "rel_ranfa_work_technique",
            joinColumns = {
                    @JoinColumn(name = "ranfa_work_id",referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ranfa_technique_id",referencedColumnName = "id")
            }
    )
    private Set<BusRanfaTechnique> ranfaTechniques=new LinkedHashSet<>();

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="gmt_audit")
    private Date gmtAudit;

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


    @JsonIgnore
    @Column(name="setting_id",columnDefinition="1")
    private Long settingId;

    @JsonIgnore
    @OneToOne(targetEntity = SysSetting.class)
    @JoinColumn(name = "setting_id",referencedColumnName = "id",insertable = false,updatable = false)
    private SysSetting setting;

    @JsonIgnore
    @Column(name="pay_goods_parent_name",columnDefinition="ranfa")
    private String payGoodsParentName;

    @JsonIgnore
    @OneToMany(targetEntity = PayGoodsInOrder.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "pay_goods_parent_name",referencedColumnName = "pay_goods_parent_name",insertable = false,updatable = false)
    @JoinColumn(name = "goods_id",referencedColumnName = "id",insertable = false,updatable = false)
    private Set<PayGoodsInOrder> payGoodsInOrders;


    @JsonIgnore
    @OneToOne(targetEntity = PayGoodsBuy.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "pay_goods_parent_name",referencedColumnName = "pay_goods_parent_name",insertable = false,updatable = false)
    @JoinColumn(name = "id",referencedColumnName = "goods_id",insertable = false,updatable = false)
    private PayGoodsBuy payGoodsBuy;



    @JsonIgnore
    @OneToOne(targetEntity = PayGoodsCollection.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "pay_goods_parent_name",referencedColumnName = "pay_goods_parent_name",insertable = false,updatable = false)
    @JoinColumn(name = "id",referencedColumnName = "goods_id",insertable = false,updatable = false)
    private PayGoodsCollection payGoodsCollection;

    @OneToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "user_id_by_upload",referencedColumnName = "id",insertable = false,updatable = false)
    private SysUser userByUpload;

    @OneToMany(targetEntity = BusRanfaWorkVideo.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "ranfa_work_id",referencedColumnName = "id")
    private Set<BusRanfaWorkVideo> ranfaWorkVideos;

    @org.springframework.data.annotation.Transient
    private Boolean buy;

    @org.springframework.data.annotation.Transient
    private Boolean collection;


    /**
     * 染发前图片访问路径
     * @return
     */
    public String getFileUrlByFrontCoverImage(){
        if(getSetting()==null){
            return null;
        }
        return getSetting().getFileVisitUrl()+fileIdByFrontCoverImage;
    }


    /**
     * 染发后图片访问路径
     * @return
     */
    public String getFileUrlByBackCoverImage(){
        if(getSetting()==null){
            return null;
        }
        return  getSetting().getFileVisitUrl()+fileIdByBackCoverImage;
    }

    /**
     * 获取作品中的视频列表
     * 根据 集 来正序获取
     * @return
     */
    public List<String> getFileUrlsOfVideo(){

        if(getSetting()==null){
            return null;
        }

        List<BusRanfaWorkVideo> ranfaWorkVideoList=new ArrayList<BusRanfaWorkVideo>();
        for (BusRanfaWorkVideo ranfaWorkVideo : getRanfaWorkVideos()) {
            ranfaWorkVideoList.add(ranfaWorkVideo);
        }
        Collections.sort(ranfaWorkVideoList, new Comparator<BusRanfaWorkVideo>() {
            @Override
            public int compare(BusRanfaWorkVideo o1, BusRanfaWorkVideo o2) {
                return o1.getEpisode()-o2.getEpisode();
            }
        });
        List<String> result=new ArrayList<String>();
        for (BusRanfaWorkVideo cesRanfaWorkVideo : ranfaWorkVideoList) {
            result.add(cesRanfaWorkVideo.getFileUrlOfVideo());
        }

        return result;
    }


}
