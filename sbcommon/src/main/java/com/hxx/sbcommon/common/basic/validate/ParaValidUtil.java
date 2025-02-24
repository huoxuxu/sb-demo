package com.hxx.sbcommon.common.basic.validate;

import org.apache.commons.lang3.StringUtils;

/**
 * 参数校验
 */
public class ParaValidUtil {

    /**
     * 校验入参Validator
     *
     * @param m
     * @param <T>
     * @return 成功返回null，失败返回错误信息
     */
    public static <T> String validReqPara(T m) {
        String paraInvalidErrMsg = ValidUtils.validateReturnFirstErr(m);
        if (!StringUtils.isBlank(paraInvalidErrMsg)) {
            return paraInvalidErrMsg;
        }

        return null;
    }
}
