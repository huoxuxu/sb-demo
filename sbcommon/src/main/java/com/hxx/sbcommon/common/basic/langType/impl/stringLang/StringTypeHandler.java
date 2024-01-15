package com.hxx.sbcommon.common.basic.langType.impl.stringLang;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 10:22:30
 **/
public class StringTypeHandler implements LangTypeHandler<String> {
    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public String change(Object val) {
        if (val == null) {
            return null;
        }

        // 整型
        if (val instanceof Byte) {
            return String.valueOf(val);
        }
        if (val instanceof Short) {
            return String.valueOf(val);
        }
        if (val instanceof Integer) {
            return String.valueOf(val);
        }
        if (val instanceof Long) {
            return String.valueOf(val);
        }

        // 浮点型
        if (val instanceof Float) {
            return String.valueOf(val);
        }
        if (val instanceof Double) {
            return String.valueOf(val);
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).stripTrailingZeros()
                    .toPlainString();
        }

        // 布尔型
        if (val instanceof Boolean) {
            return ((Boolean) val) ? "true" : "false";
        }

        // 字符串
        if (val instanceof String) {
            return (String) val;
        }

        return String.valueOf(val);
//        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
