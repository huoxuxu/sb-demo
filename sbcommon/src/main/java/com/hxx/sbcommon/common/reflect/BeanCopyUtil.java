package com.hxx.sbcommon.common.reflect;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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
    public static <TSrc, TTarget> void copy(TSrc src, TTarget target, boolean skipNull)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (src == null || target == null) {
            return;
        }

        Class<?> targetCls = target.getClass();
        List<Field> targetDeclaredFields = FieldUtils.getAllFieldsList(targetCls);
        Map<String, List<Field>> targetFieldMap = targetDeclaredFields.stream()
                .collect(Collectors.groupingBy(Field::getName));

        Class<?> srcCls = src.getClass();
        List<Field> declaredFields = FieldUtils.getAllFieldsList(srcCls);
        for (Field declaredField : declaredFields) {
            String fieldName = declaredField.getName();
            Object propVal = PropertyUtils.getProperty(src, fieldName);
            if (skipNull && propVal == null) {
                continue;
            }

            List<Field> targetFields = targetFieldMap.getOrDefault(fieldName, null);
            if (CollectionUtils.isEmpty(targetFields)) {
                // 字段不存在
                continue;
            }

            if (BeanTypeUtil.eq(declaredField, targetFields.get(0))) {
                // 类型要一致
                PropertyUtils.setProperty(target, fieldName, propVal);
            }
        }
    }

}
