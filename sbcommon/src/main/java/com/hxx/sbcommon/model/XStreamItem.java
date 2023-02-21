package com.hxx.sbcommon.model;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-10 17:04:54
 **/
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XStreamItem {
    String list();

    String item();

    Class<?> clazz();
}
