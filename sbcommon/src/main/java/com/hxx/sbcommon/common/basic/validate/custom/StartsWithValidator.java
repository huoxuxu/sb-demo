package com.hxx.sbcommon.common.basic.validate.custom;

import com.hxx.sbcommon.common.basic.validate.custom.attr.StartsWithAttr;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-23 16:05:46
 **/
public class StartsWithValidator implements ConstraintValidator<StartsWithAttr, String> {
    private String startsWithVal;
    private boolean not;

    @Override
    public void initialize(StartsWithAttr constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        startsWithVal = constraintAnnotation.value();
        not = constraintAnnotation.not();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(value)) {
            return false;
        }

        startsWithVal = startsWithVal == null ? "" : startsWithVal;
        return not ? !value.startsWith(startsWithVal) : value.startsWith(startsWithVal);
    }
}
