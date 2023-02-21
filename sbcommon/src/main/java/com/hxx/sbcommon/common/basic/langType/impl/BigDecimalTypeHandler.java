package com.hxx.sbcommon.common.basic.langType.impl;


import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 10:47:11
 **/
public class BigDecimalTypeHandler implements LangTypeHandler<BigDecimal> {
    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public BigDecimal convert(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof BigDecimal) {
            return (BigDecimal) val;
        }

        if (val instanceof String) {
            return new BigDecimal((String) val);
        }

        // 如果是整型、长整型、短整型
        if (val instanceof Byte) {
            return BigDecimal.valueOf((Byte) val);
        }
        if (val instanceof Short) {
            return BigDecimal.valueOf((Short) val);
        }
        if (val instanceof Integer) {
            return BigDecimal.valueOf((Integer) val);
        }
        if (val instanceof Long) {
            return BigDecimal.valueOf((Long) val);
        }

        // 浮点型
        if (val instanceof Float) {
            return BigDecimal.valueOf((Float) val);
        }
        if (val instanceof Double) {
            return BigDecimal.valueOf((Double) val);
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
