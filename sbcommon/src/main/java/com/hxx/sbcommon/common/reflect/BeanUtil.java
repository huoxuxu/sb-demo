package com.hxx.sbcommon.common.reflect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 生产可用的模型拷贝方法
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-31 10:24:15
 **/
@Slf4j
public class BeanUtil {

    /**
     * 模型转Map
     * 属性null值也会存储
     *
     * @param source
     * @param ignoreProperties
     * @return
     */
    public static Map<String, Object> toMap(Object source, String... ignoreProperties) {
        Class<?> actualEditable = source.getClass();
        PropertyDescriptor[] propDescs = BeanUtils.getPropertyDescriptors(actualEditable);
        Set<String> ignorePropNames = ignoreProperties != null
                ? new HashSet<>(Arrays.asList(ignoreProperties))
                : new HashSet<>();

        Map<String, Object> map = new HashMap<>();
        for (PropertyDescriptor propDesc : propDescs) {
            String name = propDesc.getName();
            // 过滤忽略属性
            if (ignorePropNames.contains(name)) {
                continue;
            }
            Method readMethod = propDesc.getReadMethod();
            if (readMethod == null) {
                continue;
            }

            try {
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }

                Object value = readMethod.invoke(source);
                map.put(name, value);
            } catch (Throwable ex) {
                throw new FatalBeanException(
                        "Could not get property '" + name + "' value from source", ex);
            }
        }
        return map;
    }

    /**
     * 模型属性Copy
     * 只拷贝属性类型兼容的属性
     * 属性的null值也会拷贝
     * 优先使用
     *
     * @param source
     * @param target
     * @param ignoreProperties
     */
    public static void copyTo(Object source, Object target, String... ignoreProperties) {
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        Set<String> ignoreList = ignoreProperties != null
                ? new HashSet<>(Arrays.asList(ignoreProperties))
                : new HashSet<>();

        for (PropertyDescriptor targetPd : targetPds) {
            String name = targetPd.getName();
            if (ignoreList.contains(name)) {
                continue;
            }
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }

            PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), name);
            if (sourcePd == null) {
                continue;
            }
            Method readMethod = sourcePd.getReadMethod();
            if (readMethod == null) {
                continue;
            }

            if (ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }

                    Object value = readMethod.invoke(source);
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }
                    writeMethod.invoke(target, value);
                } catch (Throwable ex) {
                    throw new FatalBeanException(
                            "Could not copy property '" + name + "' from source to target", ex);
                }
            }
        }
    }

    /**
     * Map字典拷贝到对象
     * Map中存在（区分大小写）的属性都会拷贝
     * 字典中对应的属性值为null也会拷贝
     *
     * @param source
     * @param target
     * @param ignoreProperties
     */
    public static void mapCopyTo(Map<String, Object> source, Object target, String... ignoreProperties) {
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        Set<String> ignoreList = ignoreProperties != null
                ? new HashSet<>(Arrays.asList(ignoreProperties))
                : new HashSet<>();

        for (PropertyDescriptor targetPd : targetPds) {
            String name = targetPd.getName();
            if (ignoreList.contains(name)) {
                continue;
            }
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }

            if (!source.containsKey(name)) {
                continue;
            }

            try {
                Object value = source.get(name);

                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                writeMethod.invoke(target, value);
            } catch (Throwable ex) {
                throw new FatalBeanException(
                        "Could not copy property '" + name + "' from source to target", ex);
            }
        }
    }

}
