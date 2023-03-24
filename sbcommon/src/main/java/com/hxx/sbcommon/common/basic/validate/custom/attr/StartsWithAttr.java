package com.hxx.sbcommon.common.basic.validate.custom.attr;

import com.hxx.sbcommon.common.basic.validate.custom.StartsWithValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-23 16:03:07
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.TYPE_USE})
@Constraint(validatedBy = StartsWithValidator.class)
public @interface StartsWithAttr {
    String message() default "参数校验失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * startWith的参数
     *
     * @return
     */
    String value() default "";

    /**
     * 非startWith
     *
     * @return
     */
    boolean not() default false;
}
