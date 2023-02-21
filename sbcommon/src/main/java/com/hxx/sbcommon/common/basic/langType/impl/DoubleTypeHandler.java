package com.hxx.sbcommon.common.basic.langType.impl;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 10:39:07
 **/
public class DoubleTypeHandler implements LangTypeHandler<Double> {
    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public Double convert(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Double) {
            return (Double) val;
        }

        if (val instanceof String) {
            return Double.parseDouble((String) val);
        }

        // 如果是整型、长整型、短整型
        if (val instanceof Byte) {
            return ((Byte) val).doubleValue();
        }
        if (val instanceof Short) {
            return ((Short) val).doubleValue();
        }
        if (val instanceof Integer) {
            return ((Integer) val).doubleValue();
        }
        if (val instanceof Long) {
            return ((Long) val).doubleValue();
        }

        // 浮点型
        if (val instanceof Float) {
            return ((Float) val).doubleValue();
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).doubleValue();
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
