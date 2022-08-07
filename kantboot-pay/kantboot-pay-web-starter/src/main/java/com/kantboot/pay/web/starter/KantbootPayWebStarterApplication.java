package com.kantboot.pay.web.starter;

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
public class KantbootPayWebStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(KantbootPayWebStarterApplication.class, args);
    }

}
