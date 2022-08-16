package com.kantboot.pay.web.starter.controller;

import com.kantboot.pay.module.entity.PayNotify;
import com.kantboot.pay.module.repository.PayGoodsInOrderRepository;
import com.kantboot.pay.module.repository.PayGoodsOrderRepository;
import com.kantboot.pay.module.repository.PayGoodsBuyRepository;
import com.kantboot.pay.module.service.IPayNotifyService;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.util.common.util.RestResult;
import com.kantboot.util.core.controller.BaseController;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@Slf4j
@RequestMapping("/pay_notify")
public class PayNotifyController extends BaseController<PayNotify,Long> {

    @Resource
    IPayNotifyService service;
    @Resource
    PayGoodsOrderRepository payGoodsOrderRepository;

    @Resource
    PayGoodsInOrderRepository payGoodsInOrderRepository;

    @Resource
    PayGoodsBuyRepository payGoodsRepository;
    @Resource
    ISysUserService sysUserService;

    @PostMapping("/set_default_use")
    public RestResult<?> setDefaultUse(@RequestParam("id") Long id){
        return RestResult.success(service.setDefaultUse(id),"设置成功");
    }

    @PostMapping("/get_default_use")
    public RestResult<?> getDefaultUse(){
        return RestResult.success(service.getDefaultUse(),"获取成功");
    }



    /**
     * 统一回调接口
     * @param request
     * @param response
     * @return
     */
    @SneakyThrows
    @RequestMapping("/pay_notify")
    public Object notify(HttpServletRequest request, HttpServletResponse response){
        return service.payNotify();
    }

    // 带编码的
    @SneakyThrows
    public String is2String(InputStream in) {
        StringBuffer out = new StringBuffer();
        InputStreamReader inread = new InputStreamReader(in, "UTF-8");
        char[] b = new char[4096];
        for (int n; (n = inread.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

}
