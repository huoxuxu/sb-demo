package com.hxx.sbcommon.common.basic.langType.impl.floatLang;

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
    public Double change(Object val) {
        if (val == null) {
            return null;
        }

        // 整型
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
        if (val instanceof Double) {
            return (Double) val;
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).doubleValue();
        }

        // 布尔型
        if (val instanceof Boolean) {
            return ((Boolean) val) ? 1D : 0;
        }

        // 字符串
        if (val instanceof String) {
            return Double.parseDouble((String) val);
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
