package com.hxx.sbConsole.commons.annotation;

import java.lang.annotation.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-16 14:55:27
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PrintParamAnno {
}
