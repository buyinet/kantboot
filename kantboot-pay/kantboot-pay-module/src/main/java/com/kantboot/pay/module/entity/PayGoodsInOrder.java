package com.kantboot.pay.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

/**
 * 订单中的商品
 */
@Entity
@Table(name="pay_goods_in_order")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class PayGoodsInOrder implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     */
    @Column(name="title")
    private String title;

    @Column(name="goods_id")
    private String goodsId;

    @Column(name="number",columnDefinition="1")
    private Long number;

    @Column(name="status")
    private Integer status;

    /**
     * 是否已经回调
     */
    @Column(name="is_call_back_pay_after",columnDefinition="0")
    private Boolean callBackPayAfter;

    /**
     * 商品统一管理的名称
     */
    @Column(name="pay_goods_parent_name")
    private String payGoodsParentName;

    /**
     * 支付者
     */
    @Column(name="user_id_by_pay")
    private Long userIdByPay;

    /**
     * 购买者
     */
    @Column(name="user_id_by_buy")
    private Long userIdByBuy;
    /**
     * 商品订单id
     */
    @Column(name="pay_goods_order_id")
    private Long payGoodsOrderId;

    /**
     * 对应的订单
     */
    @JsonIgnore
    @OneToOne(targetEntity = PayGoodsOrder.class,
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "pay_goods_order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PayGoodsOrder payGoodsOrders;

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



}
