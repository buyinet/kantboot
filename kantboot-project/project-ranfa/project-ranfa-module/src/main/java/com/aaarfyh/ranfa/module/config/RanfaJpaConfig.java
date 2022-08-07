package com.aaarfyh.ranfa.module.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(value = {"com.aaarfyh.ranfa"})
@EntityScan(basePackages = "com.aaarfyh.ranfa")
public class RanfaJpaConfig {
}
