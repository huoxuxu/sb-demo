package com.hxx.sbcommon.common.reflect;

import com.hxx.sbcommon.common.basic.text.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.*;
import java.util.*;

/*
  // 允许调用私有方法
  constructor.setAccessible(true);
  field.setAccessible(true);
 */

/**
 * 反射工具
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-25 18:09:25
 **/
@Slf4j
public class ReflectUseful {
    // 当前类
    private Class currentCls;

    // 所有字段
    private List<Field> fields;
    // 所有方法
    private Map<String, Method> methods;
    // 构造函数
    private Constructor[] constructors;
    // 公开的实例方法
    private List<Method> publicInstanceMethods = new ArrayList<>();
    private List<Method> publicStaticMethods = new ArrayList<>();

    // 包含Getter的方法
    private Map<String, Method> fieldGetterMap = new HashMap<>();
    // 包含Setter的方法
    private Map<String, Method> fieldSetterMap = new HashMap<>();
    // 类的属性集合
    private List<PropInfo> propInfos = new ArrayList<>();


    public ReflectUseful(Class<?> currentCls) {
        this.currentCls = currentCls;

        this.constructors = currentCls.getConstructors();

        // 方法
        this.methods = getMethodMap(currentCls);
        if (!methods.isEmpty()) {
            initMethod(this.methods.values());
        }

        // 获取类的所有属性
        this.fields = getFields(currentCls);
        if (!CollectionUtils.isEmpty(fields)) {
            this.propInfos = initField(this.fields);
        }
    }

    /**
     * 获取类属性
     *
     * @return
     */
    public List<PropInfo> getPropInfos() {
        return propInfos;
    }

    /**
     * 查询类中方法
     *
     * @param clazz
     * @return
     */
    public static Map<String, Method> getMethodMap(Class<?> clazz) {
        Map<String, Method> map = new HashMap<>();
        if (clazz == null || clazz == Object.class) {
            return map;
        }

        Method[] fieldArr = clazz.getDeclaredMethods();
        for (Method method : fieldArr) {
            String signature = MethodUtils.getSignature(method);
            map.putIfAbsent(signature, method);
        }

        Map<String, Method> methodMap = getMethodMap(clazz.getSuperclass());
        methodMap.forEach(map::putIfAbsent);
        return map;
    }

    /**
     * 拷贝bean
     *
     * @param target 拷贝目标
     * @param srcMap 源
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void copyMapTo(Object target, Map<String, Object> srcMap) throws InvocationTargetException, IllegalAccessException {
        if (target == null || srcMap == null || srcMap.isEmpty()) {
            return;
        }
        ReflectUseful reflectUseful = new ReflectUseful(target.getClass());
        List<PropInfo> propInfos = reflectUseful.getPropInfos();
        for (PropInfo propInfo : propInfos) {
            String name = propInfo.getName();
            if (!srcMap.containsKey(name)) {
                continue;
            }
            Object val = srcMap.get(name);
            if (val != null) {
                propInfo.setMethod.invoke(target, val);
            }
        }
    }

    /**
     * 拷贝bean
     *
     * @param target
     * @param source
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void copyTo(Object target, Object source) throws IllegalAccessException, InvocationTargetException {
        if (target == null || source == null) {
            return;
        }
        Map<String, Object> map = beanToMap(source);
        copyMapTo(target, map);
    }

    /**
     * bean转map
     *
     * @param target
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> beanToMap(Object target) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        if (target == null) {
            return map;
        }

        ReflectUseful reflectUseful = new ReflectUseful(target.getClass());
        List<PropInfo> propInfos = reflectUseful.getPropInfos();
        for (PropInfo propInfo : propInfos) {
            Field field = propInfo.getField();
            field.setAccessible(true);
            map.put(propInfo.getName(), field.get(target));
        }
        return map;
    }


    private static List<Field> getFields(Class<?> clazz) {
        List<Field> ls = new ArrayList<>();
        if (clazz == null || clazz == Object.class) {
            return ls;
        }

        Field[] fieldArr = clazz.getDeclaredFields();
        ls.addAll(Arrays.asList(fieldArr));

        List<Field> fs = getFields(clazz.getSuperclass());
        for (Field field : fs) {
            boolean flag = ls.stream().anyMatch(d -> field.getName().equals(d.getName()) && field.getType() == d.getType());
            if (!flag) {
                ls.add(field);
            }
        }
        return ls;
    }

    // 要先初始化 initMethod 方法
    private List<PropInfo> initField(List<Field> fields) {
        List<PropInfo> propInfos = new ArrayList<>();
        if (CollectionUtils.isEmpty(fields)) {
            return propInfos;
        }

        for (Field field : fields) {
            String name = field.getName();
            Method getMethod = fieldGetterMap.getOrDefault(name, null);
            Method setMethod = fieldSetterMap.getOrDefault(name, null);
            if (getMethod != null && setMethod != null) {
                Class<?> fieldType = field.getType();
                // get返回类型必须和字段类型一致
                Class<?> returnType = getMethod.getReturnType();
                if (fieldType != returnType) {
                    continue;
                }
                // set唯一入参类型和字段类型一致
                Class<?> parameterType = setMethod.getParameterTypes()[0];
                if (fieldType != parameterType) {
                    continue;
                }
                // add
                propInfos.add(new PropInfo(name, field, getMethod, setMethod));
            }
        }
        return propInfos;
    }

    private void initMethod(Collection<Method> methods) {
        for (Method method : methods) {
            String methodName = method.getName();

            int modifiers = method.getModifiers();
            // 公开的非静态
            if (!Modifier.isPublic(modifiers)) {
                continue;
            }

            // 非桥接
            if (method.isBridge()) {
                continue;
            }

            // 静态方法
            if (Modifier.isStatic(modifiers)) {
                publicStaticMethods.add(method);
                continue;
            }

            if (Modifier.isFinal(modifiers)) {
                continue;
            }

            publicInstanceMethods.add(method);

            // 公开的实例方法&&get开头&&无参&&非桥接
            boolean isGetter = checkGetterMethod(methodName, method);
            if (isGetter) {
                // 反推field的名字
                String fieldName;
                if (method.getReturnType() == boolean.class) {
                    if (methodName.startsWith("is")) {
                        fieldName = StringUtil.lowerFirstChar(methodName.substring(2));
                    }
                    // 按get开头处理
                    else {
                        fieldName = StringUtil.lowerFirstChar(methodName.substring(3));
                    }
                } else {
                    fieldName = StringUtil.lowerFirstChar(methodName.substring(3));
                }
                if (isValidPropertyName(fieldName)) {
                    fieldGetterMap.put(fieldName, method);
                }
            }

            // 校验此方法是否合适（公开的实例方法&&set开头&&只有一个入参无返回值&&非桥接方法）
            boolean isSetter = checkSetterMethod(methodName, method);
            if (isSetter) {
                // 反推field的名字
                String fieldName = StringUtil.lowerFirstChar(methodName.substring(3));
                if (isValidPropertyName(fieldName)) {
                    fieldSetterMap.put(fieldName, method);
                }
            }
        }
    }

    /**
     * 是否有效的属性名
     *
     * @param name
     * @return
     */
    private static boolean isValidPropertyName(String name) {
        return !name.startsWith("$") && !"serialVersionUID".equals(name) && !"class".equals(name);
    }

    /**
     * 校验是否字段的Getter 方法
     *
     * @param methodName
     * @param method
     * @return
     */
    private static boolean checkGetterMethod(String methodName, Method method) {
        // 过滤Object 类中的getXX 方法
        if ("getClass".equals(methodName)
                // 过滤方法名等于 get is
                || "get".equals(methodName)
                || "is".equals(methodName)) {
            return false;
        }

        int modifiers = method.getModifiers();
        // 公开的非静态
        if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) {
            return false;
        }

        // 非桥接
        if (method.isBridge()) {
            return false;
        }

        Class<?> returnType = method.getReturnType();
        // 如果从名字推断是getter函数
        if (!methodName.startsWith("get")) {
            if (methodName.startsWith("is") && returnType == boolean.class) {
            } else {
                return false;
            }
        }

        // 无参
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes != null && parameterTypes.length != 0) {
            return false;
        }

        // 有返回值
        if (method.getReturnType() == void.class) {
            return false;
        }
        return true;
    }

    // 校验Setter方法
    private static boolean checkSetterMethod(String methodName, Method method) {
        /*
          boolean enabled = demoCls.isEnabled();
          demoCls.setEnabled(true);
          */
        if ("set".equals(methodName)) return false;

        int modifiers = method.getModifiers();
        // 公开的非静态
        if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) {
            return false;
        }

        // 非桥接
        if (method.isBridge()) {
            return false;
        }

        // 如果从名字推断是setter函数，添加到setter函数列表
        if (!methodName.startsWith("set")) {
            return false;
        }

        // 且只有一个入参
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes != null && parameterTypes.length != 1) {
            return false;
        }

        // 无返回值
        if (method.getReturnType() != void.class) {
            return false;
        }

        return true;
    }

    @Data
    public static class PropInfo {
        /**
         * 属性名
         */
        private String name;
        /**
         * 字段
         */
        private Field field;
        private Method getMethod;
        private Method setMethod;

        public PropInfo() {
        }

        public PropInfo(String name, Field field, Method getMethod, Method setMethod) {
            this.name = name;
            this.field = field;
            this.getMethod = getMethod;
            this.setMethod = setMethod;
        }

    }
}
