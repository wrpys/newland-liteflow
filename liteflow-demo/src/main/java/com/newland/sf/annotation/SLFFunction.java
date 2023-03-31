package com.newland.sf.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 函数定义
 *
 * @author WRP
 * @since 2023/3/28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SLFFunction {
    /**
     * 函数名称
     */
    String name();

    /**
     * 函数版本
     */
    String version();
}