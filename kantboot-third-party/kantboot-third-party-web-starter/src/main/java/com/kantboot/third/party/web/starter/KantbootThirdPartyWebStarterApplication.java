package com.kantboot.third.party.web.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.kantboot"})
@ServletComponentScan(basePackages ={"com.kantboot"} )
@Slf4j
public class KantbootThirdPartyWebStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(KantbootThirdPartyWebStarterApplication.class, args);
    }

}
