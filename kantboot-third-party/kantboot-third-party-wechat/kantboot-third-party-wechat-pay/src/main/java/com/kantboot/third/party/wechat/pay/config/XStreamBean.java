package com.kantboot.third.party.wechat.pay.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XStreamBean {

    @Bean
    public XStream xStream(){
        XStream xStream=new XStream(new Xpp3Driver(new NoNameCoder()));
        xStream.autodetectAnnotations(true);
        return xStream;
    }
}
