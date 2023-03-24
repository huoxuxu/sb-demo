package com.hxx.sbcommon.common.basic.validate;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.HibernateValidator;

import javax.validation.*;
import java.util.Collection;
import java.util.Set;

/**
 * 模型校验
 * 示例：ValidateUtil.validate(new User());
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-23 11:18:32
 **/
public class ValidateUtil {
    private static final ValidatorFactory validatorFactory;
    private static final Validator validator;

    static {
        validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                //.addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();

        validator = validatorFactory.getValidator();
    }

    /**
     * 校验实体类，返回第一个校验失败的信息
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String validateModel(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return null;
        }

        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            return constraintViolation.getMessage();
        }
        return null;
    }

    /**
     * 校验实体类
     */
    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return;
        }

        StringBuilder validateError = new StringBuilder();
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            validateError.append(constraintViolation.getMessage())
                    .append(";");
        }

        throw new ValidationException(validateError.toString());
    }

    /**
     * 通过组来校验实体类
     */
    public static <T> void validate(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t, groups);
        if (constraintViolations.size() > 0) {
            StringBuilder validateError = new StringBuilder();
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                validateError.append(constraintViolation.getMessage())
                        .append(";");
            }

            throw new ValidationException(validateError.toString());
        }
    }

    /**
     * 通过组来校验实体类集合
     */
    public static <T> void validate(Collection<T> tCollection, Class<?>... groups) {
        if (CollectionUtils.isEmpty(tCollection)) {
            return;
        }

        for (T t : tCollection) {
            validate(t, groups);
        }
    }

}
