package com.aaarfyh.ranfa.web.starter.controller;

import com.aaarfyh.ranfa.module.entity.BusRanfaWork;
import com.aaarfyh.ranfa.module.entity.BusRanfaWorkVideo;
import com.aaarfyh.ranfa.module.repository.BusRanfaWorkVideoRepository;
import com.aaarfyh.ranfa.module.service.IBusRanfaWorkService;
import com.kantboot.util.common.util.RestResult;
import com.kantboot.util.core.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ranfa_work_video")
public class BusRanfaWorkVideoController extends BaseController<BusRanfaWorkVideo,Long> {

    @Resource
    IBusRanfaWorkService busRanfaWorkService;
    @Resource
    BusRanfaWorkVideoRepository busRanfaWorkVideoRepository;
    
    @PostMapping("/visit")
    public RestResult<Boolean> change(@RequestParam("fileId") Long fileId) {
//        System.out.println("======================"+ fileId);
//        return RestResult.success(true, "可查看");
        BusRanfaWorkVideo byId = busRanfaWorkVideoRepository.findByFileIdOfVideo(fileId);
//        System.out.println("JSON.toJSONString(byId) = " + JSON.toJSONString(byId));
        BusRanfaWork busRanfaWork = busRanfaWorkService.findById(byId.getRanfaWork()) ;
        if(busRanfaWork.getBuy()){
            return RestResult.success(true, "可查看");
        }
        return RestResult.success(false, "不可查看");

    }

}
