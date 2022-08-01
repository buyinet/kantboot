package com.kantboot.system.user.service;

import com.kantboot.system.user.entity.SysUser;
import com.kantboot.system.user.vo.LoginVO;
import com.kantboot.util.core.util.PageParam;

import java.util.List;

public interface ISysUserService {

    /**
     *  判断用户是否有权限进入对应的URI
     */
    Boolean isCanToUri();

    /**
     * 获取 用户本身 的用户信息
     * @return
     */
    SysUser getUserInfo();

    /**
     * 用户注册，注册成功之后，客户端便可保留登录状态
     * @param sysUser
     * @return
     */
    LoginVO join(SysUser sysUser);

    /**
     * 用户登录
     * @param sysUser
     * @return
     */
    LoginVO login(SysUser sysUser);

    /**
     * 退出登录
     */
    void loginOut();

    /**
     * 根据 条件 查询用户
     * @param pageParam
     * @return
     */
    List<SysUser> findAll(PageParam<SysUser> pageParam);

    /**
     * 自动生成密码
     * @return
     */
    String createUsername();
    /**
     * 添加 余额,以"分"为单位
     */
    void addBalance(Long money);

//    /**
//     * 添加 余额,以"元" 为单位
//     */
//    void addBalanceYuan(Double moneyYuan);

    /**
     * 修改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updatePassword(String oldPassword,String newPassword);

    /**
     * 用户修改个人信息
     * @return
     */
    SysUser updateUserInfo(SysUser sysUser);

}
