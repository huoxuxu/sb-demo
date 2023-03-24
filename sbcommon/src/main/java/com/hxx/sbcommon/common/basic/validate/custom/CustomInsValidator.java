package com.hxx.sbcommon.common.basic.validate.custom;

import com.hxx.sbcommon.common.basic.validate.custom.attr.CustomInsAttr;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * 自定义验证
 * 手动调用时
 * Configuration config = Validation.byProvider(PhoneValidator.class).configure();
 * ValidatorFactory vf = config.buildValidatorFactory();
 * Validator validator = vf.getValidator();
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-23 12:32:08
 **/
public class CustomInsValidator implements ConstraintValidator<CustomInsAttr, String> {
    /**
     * 自定义校验逻辑方法
     *
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
//        // 手机号验证规则：158后头随便
//        String check = "158\\d{8}";
//        Pattern regex = Pattern.compile(check);

        // 空值处理
        String val = Optional.ofNullable(value)
                .orElse("");
//        Matcher matcher = regex.matcher(val);
//        return matcher.matches();
        return val.startsWith("P");
    }

}
