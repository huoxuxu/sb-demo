package com.hxx.sbConsole.commons.annotation;

import com.hxx.sbConsole.service.common.OnFrequencyConditional;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-03 16:25:22
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnFrequencyConditional.class)
public @interface ConditionalOnFrequency {
    /**
     * 属性值
     *
     * @return
     */
    String value() default "";

    /**
     * 需要的值
     *
     * @return
     */
    String havingValue() default "";
}
