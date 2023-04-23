package com.hxx.sbcommon.common.basic.langType.impl.integerLang;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 10:26:22
 **/
public class ByteTypeHandler implements LangTypeHandler<Byte> {
    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public Byte change(Object val) {
        if (val == null) {
            return null;
        }
        // 整型
        if (val instanceof Byte) {
            return (Byte) val;
        }
        if (val instanceof Short) {
            return ((Short) val).byteValue();
        }
        if (val instanceof Integer) {
            return ((Integer) val).byteValue();
        }
        if (val instanceof Long) {
            return ((Long) val).byteValue();
        }

        // 浮点型
        if (val instanceof Float) {
            return ((Float) val).byteValue();
        }
        if (val instanceof Double) {
            return ((Double) val).byteValue();
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).byteValue();
        }

        // 布尔型
        if (val instanceof Boolean) {
            return ((Boolean) val) ? (byte) 1 : 0;
        }

        // 字符串
        if (val instanceof String) {
            return Byte.parseByte((String) val);
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
