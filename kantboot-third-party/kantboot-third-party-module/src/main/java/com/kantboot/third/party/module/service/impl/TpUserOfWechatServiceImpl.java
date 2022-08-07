package com.kantboot.third.party.module.service.impl;

import com.kantboot.system.user.module.entity.SysRole;
import com.kantboot.system.user.module.entity.SysUser;
import com.kantboot.system.user.module.repository.SysUserRepository;
import com.kantboot.system.user.module.security.TokenManage;
import com.kantboot.system.user.module.service.ISysRoleService;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.system.user.module.vo.LoginVO;
import com.kantboot.third.party.module.entity.TpUserOfWechat;
import com.kantboot.third.party.module.repository.TpUserOfWechatRepository;
import com.kantboot.third.party.module.service.ITpUserOfWechatService;
import com.kantboot.third.party.module.service.ITpWechatAppletParamService;
import com.kantboot.third.party.wechat.applet.config.WechatAppletConfig;
import com.kantboot.third.party.wechat.applet.entity.PhoneNumberInfo;
import com.kantboot.third.party.wechat.applet.service.InfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TpUserOfWechatServiceImpl implements ITpUserOfWechatService {


    @Resource
    SysUserRepository sysUserRepository;
    @Resource
    ISysUserService sysUserService;

    @Resource
    TpUserOfWechatRepository repository;
    @Resource
    TokenManage tokenManage;

    @Resource
    ISysRoleService roleService;

    @Resource
    ISysUserService service;

    @Resource
    ITpWechatAppletParamService tpWechatAppletParamService;
    public InfoService getInfoService(){
        return new InfoService().setWechatAppletConfig(tpWechatAppletParamService.getDefaultUse().getWechatAppletConfig());
    }

    @Override
    @Transactional
    public LoginVO login(String code, String encryptedData, String iv) {
        InfoService infoService=getInfoService();
        WechatAppletConfig wechatAppletConfig = infoService.getWechatAppletConfig();
        System.out.println("wechatAppletConfig = " + wechatAppletConfig);

        // 通过三个参数获取用户手机信息
        PhoneNumberInfo phoneNumberInfo = infoService.createPhoneNumberInfo(code, encryptedData, iv);
        TpUserOfWechat byUnionId = repository.findByUnionid(phoneNumberInfo.getUnionid());
        if(byUnionId==null||byUnionId.getUser()==null){
            String timeMillis = System.currentTimeMillis() + "";

            String username = sysUserService.createUsername();

            // start:获取配置的角色id
            List<SysRole> sysRoleList=roleService.roleByUserJoin();
            // end:获取配置的角色id

            SysUser sysUser = new SysUser()
                    .setRoles(sysRoleList)
                    .setPhoneNumber(phoneNumberInfo.getPurePhoneNumber())
                    .setUsername(username);

            SysUser save = sysUserRepository.save(sysUser);

            TpUserOfWechat cesAuthUserByWechat
                    = new TpUserOfWechat()
                    .setUserId(save.getId())
                    .setUnionid(phoneNumberInfo.getUnionid())
                    .setOpenid(phoneNumberInfo.getOpenid());
            TpUserOfWechat save1 = repository.save(cesAuthUserByWechat);
            String token = tokenManage.createToken(username);
            LoginVO loginVO=new LoginVO().setUsername(save.getUsername()).setUser(save).setToken(token);
            return loginVO;
        }
        SysUser user = byUnionId.getUser();
        String token = tokenManage.createToken(user.getUsername());
        LoginVO loginVO=new LoginVO().setUsername(user.getUsername()).setUser(user).setToken(token);
        return loginVO;
    }

    @Override
    public TpUserOfWechat getUserInfo() {
            SysUser userInfo = sysUserService.getUserInfo();
            Long userId = userInfo.getId();
            TpUserOfWechat byUserId = repository.findByUserId(userId);
            return byUserId;
    }

}
