package com.hxx.sbcommon.common.basic.validate.custom;

import com.hxx.sbcommon.common.basic.validate.custom.attr.ListValueAttr;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义验证
 *
 * @ListValue(values={1,2,3}) 手动调用时
 * Configuration config = Validation.byProvider(PhoneValidator.class).configure();
 * ValidatorFactory vf = config.buildValidatorFactory();
 * Validator validator = vf.getValidator();
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-23 12:32:08
 **/
public class ListValueValidator implements ConstraintValidator<ListValueAttr, Integer> {// 此处的Integer是验证字段的类型
    private Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(ListValueAttr constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
        int[] values = constraintAnnotation.values();

        for (int value : values) {
            set.add(value);
        }
    }

    /**
     * 自定义校验逻辑方法
     *
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (set.contains(value)) {
            return true;
        }
        return false;
    }

}
