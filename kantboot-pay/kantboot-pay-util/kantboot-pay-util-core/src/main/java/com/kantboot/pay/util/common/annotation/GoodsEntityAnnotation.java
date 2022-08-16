package com.kantboot.pay.util.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 每个商品的类上都得有的注解
 * parentName 代表统一名称
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GoodsEntityAnnotation {

    /**
     * 统一名称
     * @return
     */
    String parentName();
}
