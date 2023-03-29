package com.hxx.sbcommon.common.basic.validate.custom.attr;

import com.hxx.sbcommon.common.basic.validate.custom.ItemContainsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 字符串集合包含指定项
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-23 12:32:35
 **/
@Documented
// 注解的保留策略
@Retention(RetentionPolicy.RUNTIME)
// 注解的作用目标
@Target({ElementType.FIELD})
// 不同之处：于约束注解关联的验证器
@Constraint(validatedBy = ItemContainsValidator.class)
public @interface ItemContainsAttr {
    // 约束注解验证时的输出信息
    String message() default "校验失败";

    // 约束注解在验证时所属的组别
    Class<?>[] groups() default {};

    // 约束注解的有效负载（严重程度）
    Class<? extends Payload>[] payload() default {};

    /**
     * 非
     *
     * @return
     */
    boolean not() default false;
    // 自定义一个类型存放数据
    String[] values() default {};
}
