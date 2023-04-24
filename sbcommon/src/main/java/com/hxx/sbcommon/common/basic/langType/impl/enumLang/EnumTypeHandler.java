package com.hxx.sbcommon.common.basic.langType.impl.enumLang;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

/**
 * 枚举转换，
 * 支持传入：同类型枚举、枚举名称、枚举索引
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-31 16:29:52
 **/
public class EnumTypeHandler<TEnum extends Enum<TEnum>> implements LangTypeHandler<TEnum> {
    private final Class<TEnum> type;

    public EnumTypeHandler(Class<TEnum> type) {
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
    public TEnum change(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof String) {
            return Enum.valueOf(this.type, (String) val);
        }

        Class<?> valCls = val.getClass();
        if (this.type == valCls) {
            return (TEnum) val;
        }

        Integer intVal = null;
        if (val instanceof Short) {
            intVal = ((Short) val).intValue();
        }
        if (val instanceof Integer) {
            intVal = (Integer) val;
        }
        if (val instanceof Long) {
            intVal = ((Long) val).intValue();
        }

        if (intVal != null) {
            TEnum[] enumConstants = this.type.getEnumConstants();
            if (intVal >= enumConstants.length - 1) {
                throw new IllegalArgumentException("转换类型失败，提供值的索引：" + val);
            }
            return enumConstants[intVal];
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }

}
