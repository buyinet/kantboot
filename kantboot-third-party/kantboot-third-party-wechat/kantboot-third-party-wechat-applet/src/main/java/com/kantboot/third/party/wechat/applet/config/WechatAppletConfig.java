package com.kantboot.third.party.wechat.applet.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

@Data
@Accessors(chain = true)
@Configurable
public class WechatAppletConfig {

    @Value("${wechat.applet.appid}")
    private String appid;

    @Value("${wechat.applet.secret}")
    private String secret;

}
