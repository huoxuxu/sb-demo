package com.hxx.sbcommon.common.basic.validate;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidUtils {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 获取第一个校验异常
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String validateReturnFirstErr(T object) {
        Set<ConstraintViolation<T>> validateResults = validator.validate(object);
        ValidResult result = new ValidResult();
        if (CollectionUtils.isEmpty(validateResults)) {
            return null;
        }
        result.setError(true);
        for (ConstraintViolation<T> validateResult : validateResults) {
            return validateResult.getMessage();
        }
        return null;
    }

    @Data
    public static class ValidResult {

        private boolean error = false;

        private List<ValidField> errorFields = new ArrayList<>();

        public String getErrorLog() {
            List<ValidField> errorFields = getErrorFields();
            if (CollectionUtils.isEmpty(errorFields)) {
                return null;
            }
            return errorFields.stream().map(ValidField:: getErrorLog).reduce((f1, f2) -> f1 + ";  " + f2).orElse(null);
        }

        public String getErrorMsg() {
            List<ValidField> errorFields = getErrorFields();
            if (CollectionUtils.isEmpty(errorFields)) {
                return null;
            }
            return errorFields.stream().map(ValidField:: getMessage).reduce((f1, f2) -> f1 + ";  " + f2).orElse(null);
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValidField {

        private String name;

        private Object value;

        private String message;

        public String getErrorLog() {
            return "字段名：" + getName() + ", 字段值：" + getValue() + ", 错误信息：" + getMessage();
        }
    }


}
