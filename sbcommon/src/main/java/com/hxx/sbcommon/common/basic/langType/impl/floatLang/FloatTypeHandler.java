package com.hxx.sbcommon.common.basic.langType.impl.floatLang;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 10:37:17
 **/
public class FloatTypeHandler implements LangTypeHandler<Float> {
    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public Float change(Object val) {
        if (val == null) {
            return null;
        }

        // 整型
        if (val instanceof Byte) {
            return ((Byte) val).floatValue();
        }
        if (val instanceof Short) {
            return ((Short) val).floatValue();
        }
        if (val instanceof Integer) {
            return ((Integer) val).floatValue();
        }
        if (val instanceof Long) {
            return ((Long) val).floatValue();
        }

        // 浮点型
        if (val instanceof Float) {
            return (Float) val;
        }
        if (val instanceof Double) {
            return ((Double) val).floatValue();
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).floatValue();
        }

        // 布尔型
        if (val instanceof Boolean) {
            return ((Boolean) val) ? (float) 1 : 0;
        }

        // 字符串
        if (val instanceof String) {
            return Float.parseFloat((String) val);
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
