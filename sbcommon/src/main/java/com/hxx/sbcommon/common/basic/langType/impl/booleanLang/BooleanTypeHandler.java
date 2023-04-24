package com.hxx.sbcommon.common.basic.langType.impl.booleanLang;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

import java.math.BigDecimal;

/**
 * 支持 整型、浮点型、布尔型、字符串(忽略大小写的true、false)
 * 整型、浮点型数据大于0则为true，
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-26 18:07:54
 **/
public class BooleanTypeHandler implements LangTypeHandler<Boolean> {

    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public Boolean change(Object val) {
        if (val == null) {
            return null;
        }

        // 整型
        if (val instanceof Byte) {
            return ((Byte) val) > 0;
        }
        if (val instanceof Short) {
            return ((Short) val) > 0;
        }
        if (val instanceof Integer) {
            return ((Integer) val) > 0;
        }
        if (val instanceof Long) {
            return ((Long) val) > 0;
        }

        // 浮点型
        if (val instanceof Float) {
            return ((Float) val) > 0;
        }
        if (val instanceof Double) {
            return ((Double) val) > 0;
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal) val).compareTo(BigDecimal.ZERO) == 1;
        }

        // 布尔型
        if (val instanceof Boolean) {
            return (Boolean) val;
        }

        // 字符串
        if (val instanceof String) {
            return Boolean.parseBoolean((String) val);
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
