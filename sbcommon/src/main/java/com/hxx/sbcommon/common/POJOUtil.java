package com.hxx.sbcommon.common;

import java.util.function.Function;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-26 13:36:08
 **/
public class POJOUtil {
    /**
     * 字段为null时设置默认值
     * 适合常见类型和集合
     * 如果为字符串，则去除空格
     *
     * @param source
     * @param getFieldFunc
     * @param defaultVal
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> U SetNullToDefault(T source, Function<T, U> getFieldFunc, U defaultVal) {
        U fieldVal = getFieldFunc.apply(source);
        if (fieldVal == null) {
            return defaultVal;
        }

        if (defaultVal instanceof String) {
            return (U) ((String) fieldVal).trim();
        }

        return fieldVal;
    }

}
