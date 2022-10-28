package com.hxx.sbcommon.common.basic.langType.impl;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

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
    public Long convert(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Long) {
            return (Long) val;
        }

        if (val instanceof String) {
            return Long.parseLong((String) val);
        }

        // 如果是整型、长整型、短整型
        if (val instanceof Byte) {
            return ((Byte) val).longValue();
        }
        if (val instanceof Short) {
            return ((Short) val).longValue();
        }
        if (val instanceof Integer) {
            return ((Integer) val).longValue();
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
