package com.hxx.sbConsole.commons.annotation;

import java.lang.annotation.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 9:27:02
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAction {
    String name();
}
