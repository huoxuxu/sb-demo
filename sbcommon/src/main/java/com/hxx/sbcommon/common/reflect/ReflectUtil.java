package com.hxx.sbcommon.common.reflect;

import java.lang.reflect.*;

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

//    /**
//     * 生成范型参数的实例
//     */
//    public static T createInstance(){
//        ParameterizedType ptype = (ParameterizedType) this.getClass().getGenericSuperclass();
//        Class clazz = (Class<T>) ptype.getActualTypeArguments()[0];
//        T o = (T) clazz.newInstance();
//    }

//    public static T createInstance(Class genericCls){
//        Type superClass = genericCls.getGenericSuperclass();
//        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
//        Class<?> clazz = getRawType(type);
//        return (T) clazz.newInstance();
//    }

    /**
     * 获取type对应的是基类型
     * type不能直接实例化对象，通过type获取class的类型，然后实例化对象
     *
     * @param type
     * @return
     */
    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            return (Class) rawType;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + className);
        }
    }

}
