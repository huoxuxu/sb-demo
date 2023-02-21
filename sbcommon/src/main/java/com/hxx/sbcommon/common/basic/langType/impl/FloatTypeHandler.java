package com.hxx.sbcommon.common.basic.langType.impl;

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
    public Float convert(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Float) {
            return (Float) val;
        }

        if (val instanceof String) {
            return Float.parseFloat((String) val);
        }

        // 如果是整型、长整型、短整型
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
        if (val instanceof Double) {
            return ((Double) val).floatValue();
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).floatValue();
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
