package com.hxx.sbcommon.common.reflect;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-02-29 16:34:21
 **/
public class BeanCopyUtil {

    // k：类，v：类对应的属性信息
    private static final Map<String, List<ReflectUseful.PropInfo>> clsPropCache = new HashMap<>();

    private static List<ReflectUseful.PropInfo> getPropInfos(Class<?> currentCls) {
        String clsName = currentCls.getName();
        List<ReflectUseful.PropInfo> cachePropInfos = clsPropCache.getOrDefault(clsName, null);
        if (cachePropInfos == null) {
            ReflectUseful reflectUseful = new ReflectUseful(currentCls);
            List<ReflectUseful.PropInfo> propInfos = reflectUseful.getPropInfos();
            if (!CollectionUtils.isEmpty(propInfos)) {
                clsPropCache.put(currentCls.getName(), propInfos);
            }
            return propInfos;
        } else {
            return cachePropInfos;
        }
    }

    /**
     * 拷贝bean
     *
     * @param target   拷贝目标
     * @param srcMap   源
     * @param skipNull 跳过null值
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <TTarget> void copyMapTo(TTarget target, Map<String, Object> srcMap,
                                           boolean skipNull)
            throws InvocationTargetException, IllegalAccessException {
        if (target == null || srcMap == null || srcMap.isEmpty()) {
            return;
        }
        List<ReflectUseful.PropInfo> propInfos = getPropInfos(target.getClass());
        copyMapTo(target, propInfos, srcMap, skipNull);
    }


    /**
     * 将数据拷贝到模型集合
     *
     * @param cls
     * @param srcMaps
     * @param skipNull 跳过null值
     * @param <T>
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> List<T> copyMapTos(Class<T> cls, List<Map<String, Object>> srcMaps,
                                         boolean skipNull)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {
        List<T> ls = new ArrayList<>();
        if (CollectionUtils.isEmpty(srcMaps)) {
            return ls;
        }
        List<ReflectUseful.PropInfo> propInfos = getPropInfos(cls);
        for (Map<String, Object> srcMap : srcMaps) {
            T target = cls.newInstance();
            copyMapTo(target, propInfos, srcMap, skipNull);
            ls.add(target);
        }
        return ls;
    }

    /**
     * 拷贝bean
     *
     * @param target
     * @param source
     * @param skipNull 跳过null值
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <TTarget, TSrc> void copyTo(TTarget target, TSrc source, boolean skipNull)
            throws IllegalAccessException, InvocationTargetException {
        if (target == null || source == null) {
            return;
        }
        Map<String, Object> map = beanToMap(source);
        copyMapTo(target, map, skipNull);
    }

    /**
     * bean转map
     *
     * @param target
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> beanToMap(Object target) throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> map = new HashMap<>();
        if (target == null) {
            return map;
        }

        List<ReflectUseful.PropInfo> propInfos = getPropInfos(target.getClass());
        return beanToMap(target, propInfos);
    }

    /**
     * bean转Map集合
     *
     * @param targets
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <T> List<Map<String, Object>> beanToMaps(Class<T> cls, List<T> targets) throws IllegalAccessException, InvocationTargetException {
        List<Map<String, Object>> ls = new ArrayList<>();
        if (CollectionUtils.isEmpty(targets)) {
            return ls;
        }

        List<ReflectUseful.PropInfo> propInfos = getPropInfos(cls);
        for (T t : targets) {
            ls.add(beanToMap(t, propInfos));
        }
        return ls;
    }


    /**
     * @param target          目标对象
     * @param targetPropInfos 目标对象属性信息集合
     * @return
     * @throws IllegalAccessException
     */
    private static <T> Map<String, Object> beanToMap(T target, List<ReflectUseful.PropInfo> targetPropInfos)
            throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> map = new HashMap<>();
        for (ReflectUseful.PropInfo propInfo : targetPropInfos) {
            Method getMethod = propInfo.getGetMethod();
            if (getMethod != null) {
                Object propVal = getMethod.invoke(target, new Object[0]);
                map.put(propInfo.getName(), propVal);
            }
//            Field field = propInfo.getField();
//            field.setAccessible(true);
//            map.put(propInfo.getName(), field.get(target));
        }
        return map;
    }

    /**
     * @param target          目标对象
     * @param targetPropInfos 目标对象属性信息集合
     * @param srcMap          数据源
     * @param <T>
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static <T> void copyMapTo(T target, List<ReflectUseful.PropInfo> targetPropInfos, Map<String, Object> srcMap,
                                      boolean skipNull)
            throws InvocationTargetException, IllegalAccessException {
        for (ReflectUseful.PropInfo propInfo : targetPropInfos) {
            String name = propInfo.getName();
            if (!srcMap.containsKey(name)) {
                continue;
            }
            Object val = srcMap.get(name);
            if (!skipNull || val != null) {
                Method setMethod = propInfo.getSetMethod();
                if (setMethod != null) {
                    if (val == null) {
                        boolean isPrimitive = Optional.ofNullable(setMethod.getParameterTypes())
                                .map(d -> d.length == 1 ? d[0] : null)
                                .map(Class::isPrimitive)
                                .orElse(false);
                        if (isPrimitive) {
                            continue;
                        }
                    }
                    setMethod.invoke(target, val);
                }
            }
        }
    }
}
