package com.kantboot.third.party.web.starter.controller;

import com.kantboot.system.user.module.entity.SysUser;
import com.kantboot.system.user.module.repository.SysUserRepository;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.third.party.module.entity.TpUserOfWechat;
import com.kantboot.third.party.module.service.ITpWechatAppletParamService;
import com.kantboot.third.party.wechat.applet.service.InfoService;
import com.kantboot.third.party.wechat.pay.entity.PaymentToUserSmallChange;
import com.kantboot.third.party.wechat.pay.entity.PaymentToUserSmallChangeResult;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.common.util.RestResult;
import com.kantboot.system.user.module.vo.LoginVO;
import com.kantboot.third.party.module.service.ITpUserOfWechatService;
import com.kantboot.util.core.controller.BaseController;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URL;

@RequestMapping("/user_of_wechat")
@RestController
public class UserOfWechatController extends BaseController {

    @Resource
    ITpUserOfWechatService service;
    /**
     * 微信小程序登录
     * @param code
     * @param encryptedData
     * @param iv
     * @return
     */
    @RequestMapping("/login")
    public RestResult<LoginVO> loginByApplet(
            @RequestParam("code") String code,
            @RequestParam("encryptedData") String encryptedData,
            @RequestParam("iv") String iv){
        return RestResult.success(service.login(code, encryptedData, iv),"登录成功");
    }



    @Resource
    ISysUserService userService;

    @Resource
    SysUserRepository userRepository;

//    @Resource
//    ITpUserOfWechatService tpUserOfWechatService;

    @Resource
    ITpWechatAppletParamService tpWechatAppletParamService;

    public InfoService getInfoService(){
        return new InfoService().setWechatAppletConfig(tpWechatAppletParamService.getDefaultUse().getWechatAppletConfig());
    }
    @SneakyThrows
    public void cashOutToWechat(Long moeny, Long userId) {

        TpUserOfWechat userInfo = service.getUserInfo();
//        CesKantbootUser cesKantbootUser = cesKantbootUserMapper.selectById(userId);

//        CesKantbootWechatUser byUserId = cesKantbootWechatUserService.findByUserId(userId);
        PaymentToUserSmallChange paymentToUserSmallChange
                = new PaymentToUserSmallChange()
                .setAmount(moeny)
                .setOpenid(userInfo.getOpenid())
                .setDesc("匠师币-提现")
                .setIsCheckName(false);
        String fileUrlByApiclientCertP12 = tpWechatAppletParamService.getDefaultUse().getWechatPayParam().getFileUrlByApiclientCertP12();
        System.out.println("fileUrlByApiclientCertP12 = " + fileUrlByApiclientCertP12);
        PaymentToUserSmallChangeResult paymentToUserSmallChangeResult =
                paymentToUserSmallChange.createPaymentToUserSmallChangeResult(
                        tpWechatAppletParamService.getDefaultUse().getWechatPayParam().getMchId()
                        ,new URL(
                        fileUrlByApiclientCertP12
                ));
        System.out.println("openid="+userInfo.getOpenid());

//        PaymentToUserSmallChangeResult paymentToUserSmallChangeResult =
//                wechatPayConfig.createPaymentToUserSmallChangeResult(paymentToUserSmallChange);
//        System.out.println(paymentToUserSmallChangeResult);

        if(paymentToUserSmallChangeResult.isSuccess()){
            userService.addBalance(moeny);
            return;
        }

        if(paymentToUserSmallChangeResult.getErrCodeDes()!=null&&paymentToUserSmallChangeResult.getErrCodeDes().equals("余额不足")){
            paymentToUserSmallChangeResult.setErrCodeDes("老板账上没钱了，正在催他充钱~");
        }
        throw new BaseException().setState(3000).setMessage(paymentToUserSmallChangeResult.getErrCodeDes());

    }
    /**
     * 余额提现到微信
     * @return
     */
    @RequestMapping("/balance_cash_out_to_wechat")
    public RestResult<String> balanceCashOutToWechat(@RequestParam("money") Long money, HttpServletRequest request){
        SysUser userInfo = userService.getUserInfo();

        if(money>userInfo.getBalance()){
            throw new BaseException().setMessage("匠师币不足，正在刷新").setState(3001);
        }
        this.cashOutToWechat(money,userInfo.getId());
        return RestResult.success("提现成功","提现成功");
    }


}
