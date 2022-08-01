package com.kantboot.third.party.wechat.applet.entity;

import com.alibaba.fastjson.JSON;
import com.kantboot.third.party.wechat.applet.exception.Code2SessionException;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Data
@Accessors(chain = true)
public class Code2SessionKeyInfo {

    public static final String REQUEST_URL="https://api.weixin.qq.com/sns/jscode2session";
    public static final HashMap<Integer,String> ERR_MAP=new HashMap<Integer,String>();

    static {
        ERR_MAP.put(-1,"微信服务器系统繁忙");
        ERR_MAP.put(40029,"code无效");
        ERR_MAP.put(45011,"频率限制，每个用户每分钟100次");
        ERR_MAP.put(40226,"监测到微信高风险等级用户，小程序登录拦截 。");
        ERR_MAP.put(41002,"缺少appid参数");
        ERR_MAP.put(40013,"无效的appid");
        ERR_MAP.put(40125,"无效的appsecret密钥");
        ERR_MAP.put(41008,"缺少code参数");
        ERR_MAP.put(40163,"code已使用");
        ERR_MAP.put(40029,"无效的code");
    }

    private String appid;
    private String secret;
    private String code;
    private String grant_type="authorization_code";

    /**
     * 生成sessionKey
     * @return
     */
    public SessionKeyInfo createSessionKeyInfo(){

        RestTemplate restTemplate=new RestTemplate();
        SessionKeyInfo sessionKeyCommon =
                JSON.parseObject(
                    restTemplate.getForObject(
                    REQUEST_URL
                            +"?appid="+this.getAppid()
                            +"&secret="+this.getSecret()
                            +"&js_code="+this.getCode()
                            +"&grant_type="+this.getGrant_type()
                , String.class), SessionKeyInfo.class);

        if(
                sessionKeyCommon.getErrcode() == null ||
                sessionKeyCommon.getErrcode().equals(0)
        ){
            return sessionKeyCommon;
        }

        Integer errCode = sessionKeyCommon.getErrcode();
        String errMsg = ERR_MAP.get(errCode);
        throw new Code2SessionException().setErrMsg(errMsg+"\t[错误编码]"+errCode);

    }

}
