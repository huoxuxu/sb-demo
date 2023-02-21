package com.hxx.sbweb.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author
 * @description: 忽视请求结果日志打印
 * @create 2021/12/8 17:20
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseLog {
    boolean required() default true;
}
