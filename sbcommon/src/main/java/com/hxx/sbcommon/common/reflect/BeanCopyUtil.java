package com.hxx.sbcommon.common.reflect;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-02-29 16:34:21
 **/
public class BeanCopyUtil {

    /**
     * 模型转字典
     *
     * @param obj
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static Map<String, Object> toMap(Object obj)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }

        Class<?> objCls = obj.getClass();
        List<Field> declaredFields = FieldUtils.getAllFieldsList(objCls);
        for (Field declaredField : declaredFields) {
            String fieldName = declaredField.getName();
            Object propVal = PropertyUtils.getProperty(obj, fieldName);
            map.put(fieldName, propVal);
        }
        return map;
    }

    /**
     * 拷贝相同名称、相同类型的属性值
     *
     * @param src
     * @param target
     * @param skipNull
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static <TSrc, TTarget> void copy(TSrc src, TTarget target, boolean skipNull) {
        if (src == null || target == null) {
            return;
        }

//        Class<?> targetCls = target.getClass();
//        List<Field> targetDeclaredFields = FieldUtils.getAllFieldsList(targetCls);
//        Map<String, List<Field>> targetFieldMap = targetDeclaredFields.stream()
//                .collect(Collectors.groupingBy(Field::getName));

        Class<?> srcCls = src.getClass();
        List<Field> declaredFields = FieldUtils.getAllFieldsList(srcCls);
//        // loop
//        for (Field declaredField : declaredFields) {
//            String fieldName = declaredField.getName();
//            Object propVal = PropertyUtils.getProperty(src, fieldName);
//            if (skipNull && propVal == null) {
//                continue;
//            }
//
//            List<Field> targetFields = targetFieldMap.getOrDefault(fieldName, null);
//            if (CollectionUtils.isEmpty(targetFields)) {
//                // 字段不存在
//                continue;
//            }
//
//            if (BeanTypeUtil.eq(declaredField, targetFields.get(0))) {
//                // 类型要一致
//                PropertyUtils.setProperty(target, fieldName, propVal);
//            }
//        }

        Map<String, List<Field>> srcFieldMap = declaredFields.stream()
                .collect(Collectors.groupingBy(Field::getName));
        // exec
        copyUseConsumer(target, d -> {
            String fieldName = d.getName();
            // 数据源中存在
            Field srcField = Optional.ofNullable(srcFieldMap.get(fieldName))
                    .map(f -> f.isEmpty() ? null : f.get(0))
                    .orElse(null);
            if (srcField == null) {
                return;
            }

            // 类型一致
            if (!BeanTypeUtil.eq(d, srcField)) {
                return;
            }

            try {
                Object val = PropertyUtils.getProperty(src, fieldName);
                if (skipNull && val == null) {
                    return;
                }

                PropertyUtils.setProperty(target, fieldName, val);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 从Map中填充模型
     *
     * @param target
     * @param srcMap
     * @param <T>
     */
    public static <T> void copyFromMap(T target, Map<String, Object> srcMap, boolean skipNull) {
        copyUseConsumer(target, d -> {
            String fieldName = d.getName();
            if (!srcMap.containsKey(fieldName)) {
                return;
            }

            Object val = srcMap.get(fieldName);
            if (skipNull && val == null) {
                return;
            }

            try {
                PropertyUtils.setProperty(target, fieldName, val);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * @param target
     * @param fieldValFunc 根据字段取对应字段的值
     * @param <T>
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static <T> void copy(T target, Function<Field, Object> fieldValFunc)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class<?> targetCls = target.getClass();
        List<Field> targetDeclaredFields = FieldUtils.getAllFieldsList(targetCls);
        // loop
        for (Field targetDeclaredField : targetDeclaredFields) {
            String fieldName = targetDeclaredField.getName();
            // func取值
            Object val = fieldValFunc.apply(targetDeclaredField);
            if (val != null) {
                PropertyUtils.setProperty(target, fieldName, val);
            }
        }
    }

    /**
     * @param target
     * @param fieldConsumer
     * @param <T>
     */
    public static <T> void copyUseConsumer(T target, Consumer<Field> fieldConsumer) {
        Class<?> targetCls = target.getClass();
        List<Field> targetDeclaredFields = FieldUtils.getAllFieldsList(targetCls);
        // loop
        for (Field targetDeclaredField : targetDeclaredFields) {
            fieldConsumer.accept(targetDeclaredField);
        }
    }

}
