package com.hxx.sbcommon.common.basic.validate.custom;

import com.hxx.sbcommon.common.basic.validate.custom.attr.ItemContainsAttr;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

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
@Slf4j
public class ItemContainsValidator implements ConstraintValidator<ItemContainsAttr, List<String>> {
    private boolean not;
    private Set<String> set = new HashSet<>();

    @Override
    public void initialize(ItemContainsAttr constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
        String[] values = constraintAnnotation.values();

        set.addAll(Arrays.asList(values));
        not = constraintAnnotation.not();
    }

    /**
     * 自定义校验逻辑方法
     *
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        try {
            final List<String> vals = Optional.ofNullable(value)
                    .orElse(new ArrayList<>());
            boolean flag = set.stream()
                    .anyMatch(d -> vals.contains(d));

            return not ? !flag : flag;
        } catch (Exception ex) {
            log.error("入参校验出现异常：{}", ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

}
