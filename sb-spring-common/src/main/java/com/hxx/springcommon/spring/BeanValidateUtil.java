package com.hxx.springcommon.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @Valid 是 JSR 标准 API
 * @Validated 是 Spring 增加的
 * 嵌套对象需要使用 @Valid 进行标注
 * Spring-mvc 中在 @RequestBody 后面加 @Validated、@Valid 约束即可生效
 * 类上加上 @Validated 注解 方法入参加 @Validated、@Valid 约束即可生效
 */
@Slf4j
public class BeanValidateUtil {
    private static final Validator validator;

    static {
        // 获取校验器
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    /**
     * 验证模型，获取第一个校验异常
     *
     * @param model
     * @param <T>
     * @return 验证成功返回null，失败返回第一个异常信息
     */
    public static <T> String valid(T model) {
        Set<ConstraintViolation<T>> validateResults = validator.validate(model);
        if (!CollectionUtils.isEmpty(validateResults)) {
            for (ConstraintViolation<T> validateResult : validateResults) {
                return validateResult.getMessage();
            }
        }
        return null;
    }

    /**
     * 校验分组
     *
     * @param model
     * @param groupCls 待校验的分组类
     * @param <T>
     * @return
     */
    public static <T> String validGroup(T model, Class<?> groupCls) {
        /*
            @NotEmpty(message = "审批人不能为空"， group = AddMerchantMainApplyCmd.class)
            private String operator;
        */
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(model, groupCls);
        return constraintViolations.iterator().next().getMessage();
    }

}
