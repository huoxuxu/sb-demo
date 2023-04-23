package com.hxx.sbcommon.common.basic.langType.impl.integerLang;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 10:33:44
 **/
public class LongTypeHandler implements LangTypeHandler<Long> {
    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public Long change(Object val) {
        if (val == null) {
            return null;
        }

        // 整型
        if (val instanceof Byte) {
            return ((Byte) val).longValue();
        }
        if (val instanceof Short) {
            return ((Short) val).longValue();
        }
        if (val instanceof Integer) {
            return ((Integer) val).longValue();
        }
        if (val instanceof Long) {
            return (Long) val;
        }

        // 浮点型
        if (val instanceof Float) {
            return ((Float) val).longValue();
        }
        if (val instanceof Double) {
            return ((Double) val).longValue();
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).longValue();
        }

        // 布尔型
        if (val instanceof Boolean) {
            return ((Boolean) val) ? 1L : 0;
        }

        // 字符串
        if (val instanceof String) {
            return Long.parseLong((String) val);
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
