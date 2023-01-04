package com.hxx.sbcommon.common.reflect;

import com.hxx.sbcommon.common.basic.OftenUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.util.*;

/**
 * 对象的字段Setter反射
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-30 8:59:55
 **/
@Slf4j
public class ReflectorObjSetter {
    // setterMap锁
    private final static Object lockObj = new Object();
    // k=类名，v=所有Setter
    private final static Map<String, List<FieldSetter>> setterMap = new HashMap<>();

    // 类
    private final Class<?> cls;
    // 类名
    private final String className;
    // 默认无参构造函数
    private Constructor<?> defaultConstructor;


    public ReflectorObjSetter(Class<?> clazz) {
        this.cls = clazz;
        this.className = clazz.getName();

        // 初始化Setter
        if (!setterMap.containsKey(this.className)) {
            synchronized (lockObj) {
                if (!setterMap.containsKey(this.className)) {
                    this.initSetters();
                }
            }
        }

        this.addDefaultConstructor(clazz);

    }

    /**
     * 设置对象实例的字段值
     *
     * @param obj    对象
     * @param objVal 对象的字段和字段值的map
     * @param <T>    对象类型
     */
    public <T> void setInstance(T obj, Map<String, Object> objVal) {
        // 从缓存获取接口类的setter列表
        List<FieldSetter> setters = setterMap.get(className);
        if (setters == null) {
            throw new IllegalStateException("未找到类的Setter：" + className);
        }

        // 遍历接口类的setter
        for (FieldSetter setter : setters) {
            // 用setter的名字（也就是字段的名字）检索键值对
            String fieldName = setter.getFieldName();
            Object fieldValue = objVal.getOrDefault(fieldName, null);
            // 没有检索到键值对、或者键值对没有赋值，跳过
            if (fieldValue == null) continue;

            try {
                Method method = setter.getMethod();
                // 用类反射方式调用setter
                method.invoke(obj, fieldValue);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error("It's failed to initialize field: {}, reason: {}", fieldName, e);
                // 不能调用setter，可能是虚拟机回收了该子类的全部实例、入口地址变化，更新地址、再试一次
                try {
                    Method method = this.getSetter(fieldName, setter.getMethodParameterType());
                    if (method == null) {
                        throw new IllegalStateException("未找到字段" + fieldName + "对应的Setter方法，类：" + className);
                    }

                    setter.setMethod(method);
                    method.invoke(obj, fieldValue);
                } catch (Exception e1) {
                    log.info("It's failed to initialize field: {}, reason: {}", fieldName, e1);
                }
            } catch (Exception e) {
                log.error("It's failed to initialize field: {}, reason: {}", fieldName, e);
            }
        }
    }

    /**
     * 设置对象实例的字段值
     *
     * @param objVal 对象的字段和字段值的map
     * @param <T>    对象类型
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> T setInstance(Map<String, Object> objVal) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (defaultConstructor == null) {
            throw new IllegalAccessException("请确保包含无参构造，类：" + className);
        }

        T obj = (T) defaultConstructor.newInstance();
        setInstance(obj, objVal);
        return obj;
    }

    // 初始化字段Setter
    private List<FieldSetter> initSetters() {
        List<FieldSetter> setters = new ArrayList<>();
        // 遍历类的可调用函数
        for (Method method : cls.getMethods()) {
            String methodName = method.getName();

            // 校验此方法是否合适（公开的实例方法&&set开头&&只有一个入参无返回值&&非桥接方法）
            {
                int modifiers = method.getModifiers();
                // 公开的非静态
                if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)) {
                    continue;
                }

                // 如果从名字推断是setter函数，添加到setter函数列表
                if (!methodName.startsWith("set")) {
                    continue;
                }

                // 且只有一个入参
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes != null && parameterTypes.length != 1) {
                    continue;
                }

                // 无返回值
                if (method.getReturnType() != void.class) {
                    continue;
                }

                // 非桥接
                if (method.isBridge()) {
                    continue;
                }
            }

            // 反推field的名字
            String fieldName = OftenUtil.StringUtil.lowerFirstChar(methodName.substring(3));
            setters.add(new FieldSetter(fieldName, method));
        }

        // 缓存类的setter函数列表
        setterMap.put(this.className, setters);
        // 返回可调用的setter函数列表
        return setters;
    }

    // 获取字段Setter
    private Method getSetter(String fieldName, Class<?>... parameterType) throws SecurityException, NoSuchMethodException {
        String methodName = String.format("set%s", OftenUtil.StringUtil.upperFirstChar(fieldName));
        // 获取field的setter，只要是用public修饰的setter、不管是自己的还是从父类继承的，都能取到
        return cls.getMethod(methodName, parameterType);
    }

    // 添加无参构造
    private void addDefaultConstructor(Class<?> clazz) {
        Constructor<?>[] consts = clazz.getDeclaredConstructors();

        for (Constructor<?> constructor : consts) {
            if (constructor.getParameterTypes().length != 0) {
                continue;
            }

//                // 访问私有构造函数
//                if (canAccessPrivateMethods()) {
//                    try {
//                        constructor.setAccessible(true);
//                    } catch (Exception ignored) {
//                    }
//                }
//
//                if (constructor.isAccessible()) {
            this.defaultConstructor = constructor;
//                }
        }
    }


    private static boolean canAccessPrivateMethods() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }

            return true;
        } catch (Exception var1) {
            return false;
        }
    }

    @lombok.Data
    static class FieldSetter {
        /**
         * 字段名
         */
        private String fieldName;
        /**
         * 方法
         */
        private Method method;
        /**
         * 方法入参
         */
        private Class<?> methodParameterType;

        /**
         * @param fieldName 字段名
         * @param method    字段对应的set方法
         */
        public FieldSetter(String fieldName, Method method) {
            this.fieldName = fieldName;
            this.method = method;
            this.methodParameterType = this.method.getParameterTypes()[0];
        }
    }

}
