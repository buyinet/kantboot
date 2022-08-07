package com.kantboot.system.user.module.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单实体
 */
@Entity
@Table(name="sys_menu")
@Getter
@Setter
@DynamicInsert(true)
@DynamicUpdate(true)
@Accessors(chain = true)
@EqualsAndHashCode
@NoArgsConstructor
public class SysMenu  implements Serializable {

    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    /**
     * 路径
     */
    @Column(name="path")
    private String path;

    /**
     * 名称
     */
    @Column(name="name")
    private String name;

    /**
     * 图标
     */
    @Column(name="icon")
    private String icon;

    /**
     * 是否为目录
     */
    @Column(name="is_folder",columnDefinition="0")
    private Boolean folder;

    /**
     * 菜单名称
     */
    @Column(name ="label")
    private String label;

    /**
     * 页面路径
     */
    @Column(name ="page_path")
    private String pagePath;

    /**
     * 优先级
     */
    @Column(name="priority",columnDefinition="0")
    private Long priority;

    /**
     * 父id
     */
    @Column(name="pid")
    private Long pid;

    /**
     * 是否显示
     */
    @Column(name="is_show",columnDefinition="1")
    private Boolean show;

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

    @ManyToMany(
            targetEntity = SysRole.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "rel_menu_role",
            joinColumns = {
                    @JoinColumn(name = "menu_id",referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id",referencedColumnName = "id")
            }
    )
    private List<SysRole> roles;

    @OneToMany(targetEntity = SysMenu.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "pid",referencedColumnName = "id")
    private List<SysMenu> children;

}
