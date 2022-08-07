package com.kantboot.pay.module.entity;

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
import java.util.List;

/**
 * 商品订单表
 */
@Entity
@Table(name = "pay_goods_order")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class PayGoodsOrder {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 价格
     */
    @Column(name = "money")
    private String money;

    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 交易金额
     *
     */
    @Column(name = "total_fee")
    private Long totalFee;


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
     * 状态 0 未支付，1 已支付
     */
    @Column(name="status")
    private Integer status;


    /**
     * 订单内商品
     */
    @OneToMany(targetEntity = PayGoodsInOrder.class,
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "pay_goods_order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<PayGoodsInOrder> payGoodsInOrders;

    /**
     * 描述
     */
    @Column(name = "content")
    private String content;


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
