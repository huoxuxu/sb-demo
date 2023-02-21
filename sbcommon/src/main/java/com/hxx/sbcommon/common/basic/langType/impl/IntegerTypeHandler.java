package com.hxx.sbcommon.common.basic.langType.impl;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

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
    public Integer convert(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Integer) {
            return (Integer) val;
        }

        if (val instanceof String) {
            return Integer.parseInt((String) val);
        }

        // 如果是整型、长整型、短整型
        if (val instanceof Byte) {
            return ((Byte) val).intValue();
        }
        if (val instanceof Short) {
            return ((Short) val).intValue();
        }
        if (val instanceof Long) {
            return ((Long) val).intValue();
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
