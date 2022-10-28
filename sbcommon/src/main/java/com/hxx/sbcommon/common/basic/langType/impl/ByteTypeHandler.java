package com.hxx.sbcommon.common.basic.langType.impl;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

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
    public Byte convert(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Byte) {
            return (Byte) val;
        }

        if (val instanceof String) {
            return Byte.parseByte((String) val);
        }

        // 如果是整型、长整型、短整型
        if (val instanceof Short) {
            return ((Short) val).byteValue();
        }
        if (val instanceof Integer) {
            return ((Integer) val).byteValue();
        }
        if (val instanceof Long) {
            return ((Long) val).byteValue();
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
