package com.hxx.sbcommon.common.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public class BeanTypeUtil {


    /**
     * 判断类型是否一致
     * 支持简单类型、
     * 支持泛型：List:Integer
     * 支持嵌套泛型：List:Demo:List:Integer
     *
     * @param field1
     * @param field2
     * @return
     */
    public static boolean eq(Field field1, Field field2) {
        Class<?> rawType1 = field1.getType();
        Class<?> rawType2 = field2.getType();

        // 首先比较原始类型
        // 此处相同的泛型类型List<Integer> 字段，此处也不相等
        if (!rawType1.equals(rawType2)) {
            return false;
        }

        // 检查是否为泛型类型
        if (field1.getGenericType() instanceof ParameterizedType && field2.getGenericType() instanceof ParameterizedType) {
            ParameterizedType pType1 = (ParameterizedType) field1.getGenericType();
            ParameterizedType pType2 = (ParameterizedType) field2.getGenericType();

            // 比较类型参数
            Type[] actualTypeArgs1 = pType1.getActualTypeArguments();
            Type[] actualTypeArgs2 = pType2.getActualTypeArguments();

            // 简单比较泛型参数的数量和类型（这里简化处理，实际情况可能需要递归比较嵌套泛型）
            return Arrays.equals(actualTypeArgs1, actualTypeArgs2);
        }

        // 非泛型类型或泛型不匹配时返回false
        return true; // 如果原始类型相同且非泛型或已确认泛型参数匹配，则返回true
    }

}
