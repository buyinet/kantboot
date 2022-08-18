package com.kantboot.file.module.config;

import org.springframework.web.client.RestTemplate;

public class FileRestTemplateConfig {

    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}