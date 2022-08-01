package com.kantboot.system.web.starter.controller;


import com.kantboot.project.util.common.util.RestResult;
import com.kantboot.system.user.entity.SysUser;
import com.kantboot.system.user.service.ISysUserService;
import com.kantboot.system.user.vo.LoginVO;
import com.kantboot.util.core.controller.BaseController;
import com.kantboot.util.core.util.PageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p>
 * 用户表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/user")
@Api(tags = "关于`用户`的控制器")
@Slf4j
public class UserController extends BaseController<SysUser,Long> {

    @Resource
    ISysUserService sysUserService;

    /**
     * 注册用户
     * 注册成功后，便自动自动生成账号，且生成一个token记录用户登录状态，
     * 客户端便可直接通过此token进入登录状态
     * @param sysUser
     * @return
     */
    @ApiOperation(
            value="注册用户",
            notes= "注册成功后，便自动自动生成账号，且生成一个token记录用户登录状态，" +
                    "客户端便可直接通过此token进入登录状态",
            produces=MediaType.APPLICATION_JSON_VALUE,
            consumes=MediaType.APPLICATION_JSON_VALUE
    )

    @PostMapping(value = "/join",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public RestResult<LoginVO> join(
            @RequestBody SysUser sysUser
    ){
        return RestResult.success( sysUserService.join(sysUser),"注册成功");
    }

    /**
     * 用户登录
     * 登录成功后，便会返回一个token，需保存放进 key 为 token 的请求头，便可有身份验证，及对应访问权限
     * @param sysUser 登录时传来的信息,通常只需要填写账号和密码
     */
    @ApiOperation(
            value= "用户登录",
            notes = "登录成功后，便会返回一个token，需保存放进 key 为 token 的请求头，便可有身份验证，及对应访问权限"
    )

    @ApiImplicitParams(
            value = {
                @ApiImplicitParam(name="username", required=true, paramType = "body",dataTypeClass = String.class),
                @ApiImplicitParam(name="password", required=true, paramType = "body",dataTypeClass = String.class)})
    @PostMapping(value = "/login",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public RestResult<?> login(
            @RequestBody SysUser sysUser){
        return RestResult.success( sysUserService.login(sysUser),"登录成功");
    }

    /**
     * 获取用户信息
     */
    @ApiOperation(
            value= "获取用户本身的信息",
            notes = "主要原理是通过请求头中token解析出用户的用户名，" +
                    "再通过此用户名来进一步获取用户的个人信息")
    @PostMapping(value = "/get_user_info",consumes = {MediaType.ALL_VALUE})
    public RestResult<?> getUserinfo(){
        return RestResult.success( sysUserService.getUserInfo(),"获取成功");
    }

    /**
     * 退出登录
     */
    @ApiOperation(value= "退出登录")
    @PostMapping(value = "/login_out",consumes = {MediaType.ALL_VALUE})
    public RestResult<?> loginOut(){
        sysUserService.loginOut();
        return RestResult.success("已成功退出登录","已成功退出登录");
    }

    /**
     * 查询所有
     * @param pageParam
     * @return
     */
    @ApiOperation(value= "查询所有用户",
            notes = "除了对应的ID等字段采用精准查询，其余大部分字段都通过模糊查询的方式进行查询")
    @PostMapping(value = "/find_all",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public RestResult<?> findAll(@RequestBody PageParam<SysUser> pageParam){
        return RestResult.success(sysUserService.findAll(pageParam),"获取成功");
    }

    /**
     * 用户添加余额，以'分'为单位
     * @return
     */
    @ApiOperation(value= "用户添加余额，以'分为单位'")
    @PostMapping(value = "/add_balance",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public RestResult<?> addBalance(@RequestParam("money") Long money){
        sysUserService.addBalance(money);
        return RestResult.success("余额添加成功","余额添加成功");
    }

    /**
     * 用户添加余额，以'元'为单位
     * @return
     */
//    @ApiOperation(value= "用户添加余额，以'分为单位'")
//    @PostMapping(value = "/add_balance_yuan",
//            consumes = {MediaType.APPLICATION_JSON_VALUE})
//    public RestResult<?> addBalanceYuan(@RequestParam("money") Double money){
//        sysUserService.addBalanceYuan(money);
//        return RestResult.success("余额添加成功","余额添加成功");
//    }

    /**
     * 修改密码
     * @return
     */
    @ApiOperation(value= "用户修改密码")
    @PostMapping(value = "/update_password",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public RestResult<?> updatePassword(@RequestParam("oldPassword") String oldPassword,
                                        @RequestParam("newPassword") String newPassword){
        sysUserService.updatePassword(oldPassword,newPassword);
        return RestResult.success("修改密码成功","修改密码成功");
    }


    /**
     * 用户修改信息
     * @return
     */
    @ApiOperation(value= "用户修改信息")
    @PostMapping(value = "/update_user_info",
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public RestResult<?> updateUserInfo(@RequestBody SysUser sysUser){
        sysUserService.updateUserInfo(sysUser);
        return RestResult.success("修改信息成功","修改信息成功");
    }


}

