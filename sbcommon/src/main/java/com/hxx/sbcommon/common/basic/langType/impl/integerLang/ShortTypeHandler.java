package com.hxx.sbcommon.common.basic.langType.impl.integerLang;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 10:30:27
 **/
public class ShortTypeHandler implements LangTypeHandler<Short> {
    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public Short change(Object val) {
        if (val == null) {
            return null;
        }
        // 整型
        if (val instanceof Byte) {
            return ((Byte) val).shortValue();
        }
        if (val instanceof Short) {
            return (Short) val;
        }
        if (val instanceof Integer) {
            return ((Integer) val).shortValue();
        }
        if (val instanceof Long) {
            return ((Long) val).shortValue();
        }

        // 浮点型
        if (val instanceof Float) {
            return ((Float) val).shortValue();
        }
        if (val instanceof Double) {
            return ((Double) val).shortValue();
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).shortValue();
        }

        // 布尔型
        if (val instanceof Boolean) {
            return ((Boolean) val) ? (short) 1 : 0;
        }

        // 字符串
        if (val instanceof String) {
            return Short.parseShort((String) val);
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
