package com.kantboot.system.web.starter;

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
public class KantbootSystemWebStarterApplication {

    public static void main(String[] args) {
        log.info("kantboot-system正在启动......");
        SpringApplication.run(KantbootSystemWebStarterApplication.class, args);
        log.info("kantboot-system启动完成");
    }

}
