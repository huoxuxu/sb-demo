package com.hxx.sbcommon.common.io.http.apachehttpclient;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TitansPlugin {
    /**
     * 插件执行顺序，可为负值，数值越小，越早执行
     * @return
     */
    int order() default 0;

}
