package com.hxx.sbcommon.common.reflect;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反射
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-22 10:57:01
 **/
public class ReflectUtil {

    /**
     * 反射创建数组
     *
     * @param cls    数组类型
     * @param length 数组长度
     */
    public static void createArray(Class cls, int length) {
        //创建一个元素类型为String，长度为10的数组
        Object arr = Array.newInstance(cls, length);

        //依次为arr数组中index为5、6的元素赋值
        Array.set(arr, 5, "Byte");
        Array.set(arr, 6, "科技");
        //依次取出arr数组中index为5、6的元素的值；
        Object str1 = Array.get(arr, 5);
        Object str2 = Array.get(arr, 6);
        //输出arr数组中index为5、6的元素
        System.out.print(str1);
        System.out.print(str2);
    }

    private static void procArray(Object val) {
        Class<?> cls = val.getClass();
        // 判断是否数组
        if (cls.isArray()) {
            // 获取数组长度
            int length = java.lang.reflect.Array.getLength(val);
            // 获取数组元素类型
            Class elementType = cls.getComponentType();
            System.out.println(elementType);
            // loop
            for (int i = 0; i < length; i++) {
                Object item = Array.get(val, i);
                System.out.println(item);
            }
        }
    }

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
     * 获取type对应的class类型
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
            return Array.newInstance(getRawType(componentType), 0)
                    .getClass();
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        } else {
            String className = type == null ? "null" : type.getClass()
                    .getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + className);
        }
    }

    /**
     * 获取方法签名
     * com.zto.base.bean.ConfigResponse#pageQuerySecCode:com.zto.base.bean.request.TSecCodeRequest,java.lang.Integer,java.lang.Integer
     *
     * @param method
     * @return
     */
    public static String getMethodSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName())
                    .append('#');
        }

        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();

        for (int i = 0; i < parameters.length; ++i) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }

            sb.append(parameters[i].getName());
        }

        return sb.toString();
    }

    /**
     * 创建泛型类型的实例
     *
     * @param clsT
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> T createInstance(Class clsT) throws InstantiationException, IllegalAccessException {
        return (T) clsT.newInstance();
    }
}
