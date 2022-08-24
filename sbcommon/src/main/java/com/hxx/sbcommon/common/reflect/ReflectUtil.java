package com.hxx.sbcommon.common.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-22 10:57:01
 **/
public class ReflectUtil {
    /**
     * 创建数组实例
     *
     * @param cls
     * @param arrayLength
     * @param <T>
     * @return
     */
    public <T> T[] generateArrayInstance(Class<T> cls, int arrayLength) {
        // 使用原生的反射方法，在运行时知道其数组对象类型
        @SuppressWarnings("unchecked") final T[] array = (T[]) Array.newInstance(cls, arrayLength);
        return array;
    }

    /**
     * 获取类型的范型参数类型
     *
     * @param cls
     * @param GenericArgumentIndex
     * @return
     */
    public Class<?> getActualTypeArgument(Class cls, int GenericArgumentIndex) {
        ParameterizedType genericSuperclass = (ParameterizedType) cls.getGenericSuperclass();
        Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
        Type actualTypeArgument = actualTypeArguments[GenericArgumentIndex];
        return (Class<?>) actualTypeArgument;
    }

}
