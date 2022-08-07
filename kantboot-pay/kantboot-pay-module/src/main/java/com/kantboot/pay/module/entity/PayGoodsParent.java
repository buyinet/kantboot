package com.kantboot.pay.module.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.List;
import java.util.Map;

@Entity
@Table(name="pay_goods_parent")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
public class PayGoodsParent implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     */
    @Column(name="title")
    private String title;

    /**
     * 名称
     */
    @Column(name="name")
    private String name;


    /**
     * 支付后回调地址
     * 包括校验信息是否准确
     */
    @Column(name="pay_after_url")
    private String payAfterUrl;

    /**
     * 支付前回调地址
     */
    @Column(name="pay_before_url")
    private String payBeforeUrl;

    /**
     * 描述
     */
    @Column(name="content")
    private String content;

    /**
     * 查询的参数
     */
    @Column(name="param_str")
    private String paramStr;

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


    public List<Map> getParam(){
        if(paramStr==null){
            paramStr="[]";
        }
        List<Map> param = JSON.parseArray(paramStr,Map.class);
        return param;
    }

    public PayGoodsParent setParam(List<Map<String,Object>> data){
        if(data==null){
            return this;
        }
        this.paramStr= JSON.toJSONString(data);
        return this;
    }

}
