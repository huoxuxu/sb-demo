package com.hxx.sbConsole.module.dynamicproxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName NeedProxy
 * @Description
 * @Author Silwings
 * @Date 2021/3/7 15:57
 * @Version V1.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedProxy {
    String value() default "";
}
