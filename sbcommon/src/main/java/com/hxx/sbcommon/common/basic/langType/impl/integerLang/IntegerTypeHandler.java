package com.hxx.sbcommon.common.basic.langType.impl.integerLang;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 10:32:21
 **/
public class IntegerTypeHandler implements LangTypeHandler<Integer> {
    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public Integer change(Object val) {
        if (val == null) {
            return null;
        }

        // 整型
        if (val instanceof Byte) {
            return ((Byte) val).intValue();
        }
        if (val instanceof Short) {
            return ((Short) val).intValue();
        }
        if (val instanceof Integer) {
            return (Integer) val;
        }
        if (val instanceof Long) {
            return ((Long) val).intValue();
        }

        // 浮点型
        if (val instanceof Float) {
            return ((Float) val).intValue();
        }
        if (val instanceof Double) {
            return ((Double) val).intValue();
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).intValue();
        }

        // 布尔型
        if (val instanceof Boolean) {
            return ((Boolean) val) ? 1 : 0;
        }

        // 字符串
        if (val instanceof String) {
            return Integer.parseInt((String) val);
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
