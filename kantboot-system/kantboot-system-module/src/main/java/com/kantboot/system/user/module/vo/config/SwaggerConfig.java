package com.kantboot.system.user.module.vo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class SwaggerConfig {

    /**
     * 是否启用
     */
    @Value("${swagger.enable}")
    private Boolean enable;

    /**
     * 标题
     */
    @Value("${swagger.title}")
    private String title;

    /**
     * 描述
     */
    @Value("${swagger.description}")
    private String description;

    /**
     * 服务条款Url
     */
    @Value("${swagger.termsOfServiceUrl}")
    private String termsOfServiceUrl;

    /**
     * 版本
     */
    @Value("${swagger.version}")
    private String version;

//    /**
//     * API文档配置
//     * @return
//     */
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.OAS_30).pathMapping("/")
//                .enable(enable)
//                .apiInfo(apiInfo());
//    }
//
//    /**
//     * 添加摘要信息
//     * @return
//     */
//    private ApiInfo apiInfo() {
//        // 用ApiInfoBuilder进行定制
//        return new ApiInfoBuilder()
//                .title(title)
//                .description(description)
//                .termsOfServiceUrl(termsOfServiceUrl)
//                .version(version)
//                .build();
//    }

}