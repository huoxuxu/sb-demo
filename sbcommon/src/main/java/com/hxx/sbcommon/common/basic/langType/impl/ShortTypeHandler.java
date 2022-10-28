package com.hxx.sbcommon.common.basic.langType.impl;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

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
    public Short convert(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Short) {
            return (Short) val;
        }

        if (val instanceof String) {
            return Short.parseShort((String) val);
        }

        // 如果是整型、长整型、短整型
        if (val instanceof Byte) {
            return ((Byte) val).shortValue();
        }
        if (val instanceof Integer) {
            return ((Integer) val).shortValue();
        }
        if (val instanceof Long) {
            return ((Long) val).shortValue();
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
