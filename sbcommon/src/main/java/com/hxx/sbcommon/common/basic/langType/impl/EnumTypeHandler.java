package com.hxx.sbcommon.common.basic.langType.impl;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-31 16:29:52
 **/
public class EnumTypeHandler<E extends Enum<E>> implements LangTypeHandler<E> {
    private final Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }

        this.type = type;
    }

    /**
     * @param val 待转型的值 如果传入字符串，需要确保和枚举Name大小写一致
     * @return
     */
    @Override
    public E convert(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof String) {
            return Enum.valueOf(this.type, (String) val);
        }

        Class<?> valCls = val.getClass();
        if (this.type == valCls) {
            return (E) val;
        }

        Integer ind = null;
        if (val instanceof Short) {
            ind = ((Short) val).intValue();
        }
        if (val instanceof Long) {
            ind = ((Long) val).intValue();
        }
        if (val instanceof Integer) {
            ind = (Integer) val;
        }

        if (ind != null) {
            E[] enumConstants = this.type.getEnumConstants();
            if (ind >= enumConstants.length - 1) {
                throw new IllegalArgumentException("转换类型失败，提供值的索引：" + val);
            }
            return enumConstants[ind];
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }

}
