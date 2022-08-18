package com.aaarfyh.ranfa.web.starter.controller;

import com.aaarfyh.ranfa.module.entity.BusRanfaWork;
import com.aaarfyh.ranfa.module.entity.BusRanfaWorkVideo;
import com.aaarfyh.ranfa.module.repository.BusRanfaWorkVideoRepository;
import com.aaarfyh.ranfa.module.service.IBusRanfaWorkService;
import com.alibaba.fastjson.JSON;
import com.kantboot.file.module.entity.KfmFile;
import com.kantboot.file.module.repository.KmfFileRepository;
import com.kantboot.system.user.module.entity.SysRole;
import com.kantboot.system.user.module.entity.SysUser;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.util.common.util.RestResult;
import com.kantboot.util.core.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ranfa_work_video")
public class BusRanfaWorkVideoController extends BaseController<BusRanfaWorkVideo,Long> {

    @Resource
    IBusRanfaWorkService busRanfaWorkService;
    @Resource
    BusRanfaWorkVideoRepository busRanfaWorkVideoRepository;
    @Resource
    KmfFileRepository fileRepository;

    @Resource
    ISysUserService sysUserService;
    
    @PostMapping("/visit")
    public RestResult<Boolean> visit(@RequestParam("fileId") Long fileId) {
//        System.out.println("======================"+ fileId);
//        return RestResult.success(true, "可查看");
        KfmFile kfmFile = fileRepository.findById(fileId).get();
        if(kfmFile.getGmtCreate().getTime()<(new Date().getTime()+(1000*60*60*1))){
            return RestResult.success(true, "可查看");
        }
        BusRanfaWorkVideo byId = busRanfaWorkVideoRepository.findByFileIdOfVideo(fileId);
        System.out.println(JSON.toJSONString(byId)+"===");

        //        System.out.println("JSON.toJSONString(byId) = " + JSON.toJSONString(byId));
        BusRanfaWork busRanfaWork = busRanfaWorkService.findById(byId.getRanfaWork()) ;
        if(busRanfaWork.getBuy()){
            return RestResult.success(true, "可查看");
        }

        SysUser userInfo = sysUserService.getUserInfo();
        List<SysRole> roles = userInfo.getRoles();
        for (SysRole role : roles) {
            if(role.getName().equals("ROLE_admin")){
                return RestResult.success(true, "可查看");
            }
        }
        return RestResult.success(false, "不可查看");

    }

}
