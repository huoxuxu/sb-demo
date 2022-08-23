package com.hxx.sbcommon.common.reflect;

import java.lang.reflect.Array;

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
    public <T> T[] GenerateArrayInstance(Class<T> cls, int arrayLength) {
        // 使用原生的反射方法，在运行时知道其数组对象类型
        @SuppressWarnings("unchecked") final T[] array = (T[]) Array.newInstance(cls, arrayLength);
        return array;
    }

}
