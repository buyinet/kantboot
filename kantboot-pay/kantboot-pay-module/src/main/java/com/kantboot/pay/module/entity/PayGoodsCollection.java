package com.kantboot.pay.module.entity;


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
import java.io.Serializable;
import java.util.Date;

/**
 * 商品的收藏
 */
@Entity
@Table(name="pay_goods_collection")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class PayGoodsCollection implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private Long userId;

    /**
     * 商品统一管理的名称
     */
    @Column(name="pay_goods_parent_name")
    private String payGoodsParentName;

    @Column(name="goods_id")
    private String goodsId;

    @Column(name="number")
    private Long number;

    @Column(name="title")
    private String title;

    /**
     * 创建时间
     */
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "gmt_create", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="gmt_modified",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;


}
