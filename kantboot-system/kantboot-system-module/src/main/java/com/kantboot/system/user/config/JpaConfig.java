package com.kantboot.system.user.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(value = {"com.kantboot"})
@EntityScan(basePackages = "com.kantboot")
public class JpaConfig {
}
