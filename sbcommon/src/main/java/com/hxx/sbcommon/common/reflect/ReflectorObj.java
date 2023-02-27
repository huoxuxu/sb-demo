package com.hxx.sbcommon.common.reflect;

import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;
import com.hxx.sbcommon.common.basic.langType.LangTypeHandlerFactory;
import com.hxx.sbcommon.common.reflect.demo.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 对象的字段Setter反射
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-30 8:59:55
 **/
@Slf4j
public class ReflectorObj {
    // 类信息初始化锁
    private final static Object lockObj = new Object();
    // 对象Getter k=类名，v=所有Getter
    private final static Map<String, List<FieldGetter>> getterMap = new HashMap<>();
    // 对象Setter k=类名，v=所有Setter
    private final static Map<String, List<FieldSetter>> setterMap = new HashMap<>();

    // 公开的实例方法
    private final static Map<String, List<Method>> publicInstanceMethodMap = new HashMap<>();
    // 公开的静态方法
    private final static Map<String, List<Method>> publicStaticMethodMap = new HashMap<>();

    // 类
    private final Class<?> cls;
    // 类名
    private final String className;
    // 默认无参构造函数
    private Constructor<?> defaultConstructor;


    public ReflectorObj(Class<?> clazz) {
        this.cls = clazz;
        this.className = clazz.getName();

        // 初始化Getter和Setter
        if (!setterMap.containsKey(this.className)) {
            synchronized (lockObj) {
                if (!setterMap.containsKey(this.className)) {
                    Method[] methods = cls.getMethods();
                    this.initGetters(methods);
                    this.initSetters(methods);
                    this.initMethod(methods);
                }
            }
        }

        // 处理无参构造
        this.addDefaultConstructor(clazz);
    }

    /**
     * 获取类的包含getter的字段
     *
     * @return
     */
    public List<String> getClsGetterFields() {
        List<FieldGetter> getters = getterMap.get(this.className);
        return getters.stream()
                .map(d -> d.getFieldName())
                .collect(Collectors.toList());
    }

    /**
     * 获取类的包含setter的字段
     *
     * @return
     */
    public List<String> getClsSetterFields() {
        List<FieldSetter> setters = setterMap.get(this.className);
        return setters.stream()
                .map(d -> d.getFieldName())
                .collect(Collectors.toList());
    }

    /**
     * 获取对象属性Map，注意：每次会更具字段名获取对应的get方法，性能差
     *
     * @param obj
     * @param <T>
     * @return
     */
    public <T> Map<String, Object> getObjMap(T obj) {
        List<FieldGetter> getters = getterMap.get(className);
        if (getters == null) {
            throw new IllegalStateException("未找到类的GetterMap：" + className);
        }

        Map<String, Object> objectMap = new HashMap<>();
        for (FieldGetter getter : getters) {
            String fieldName = getter.getFieldName();
            try {
                Method method = getter.getMethod();
                // 用类反射方式调用getter
                Object val = method.invoke(obj);
                objectMap.put(fieldName, val);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error("It's failed to initialize field: {}, reason: {}", fieldName, e);
                // 不能调用getter，可能是虚拟机回收了该子类的全部实例、入口地址变化，更新地址、再试一次
                try {
                    Method method = this.getClsGetterMethod(fieldName);
                    // 设置缓存
                    getter.setMethod(method);
                    // 用类反射方式调用getter
                    Object val = method.invoke(obj);
                    objectMap.put(fieldName, val);
                } catch (Exception e1) {
                    log.warn("It's failed to initialize field: {}, reason: {}", fieldName, e1);
                }
            } catch (Exception e) {
                log.error("It's failed to initialize field: {}, reason: {}", fieldName, e);
            }
        }

        return objectMap;
    }

    /**
     * 设置对象实例的字段值（区分字段大小写）
     *
     * @param obj    对象
     * @param objVal 对象的字段和字段值的map
     * @param <T>    对象类型
     */
    public <T> void setInstance(T obj, Map<String, Object> objVal) {
        // 从缓存获取接口类的setter列表
        List<FieldSetter> setters = setterMap.get(className);
        if (setters == null) {
            throw new IllegalStateException("未找到类的SetterMap：" + className);
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
                    Method method = this.getClsSetterMethod(fieldName, setter.getMethodParameterType());
                    // 设置缓存
                    setter.setMethod(method);
                    // 调用
                    method.invoke(obj, fieldValue);
                } catch (Exception e1) {
                    log.warn("It's failed to initialize field: {}, reason: {}", fieldName, e1);
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

    // 调用方法
    public Object callMethod(Object obj, List<Method> methods, String methodName, String[] parameterTypes, String returnType, Object[] methodParaVals) throws InvocationTargetException, IllegalAccessException {
        String methodSignature = MethodSignature.getMethodSignature(methodName, parameterTypes, returnType);

        Method method = methods.stream()
                .filter(d -> methodSignature.equals(MethodSignature.getMethodSignature(d)))
                .findFirst()
                .orElse(null);
        if (method == null) {
            throw new IllegalArgumentException("未找到对应dubbo接口的方法，方法签名：" + methodSignature);
        }

        // 调用方法
        Object result;
        // 匹配参数
        Class<?>[] paras = method.getParameterTypes();
        if (paras == null || paras.length == 0) {
            result = method.invoke(obj);
            return result;
        }

        methodParaVals = methodParaVals == null ? new Object[0] : methodParaVals;
        if (paras.length != methodParaVals.length) {
            throw new IllegalArgumentException("未找到对应dubbo接口的方法，方法入参个数不一致");
        }

        Object[] methodParaArr = new Object[paras.length];
        for (int i = 0; i < paras.length; i++) {
            Class para = paras[i];
            Object mp = methodParaVals[i];
            // 转换类型
            Object val = LangTypeHandlerFactory.convert(mp, para);
            methodParaArr[i] = val;
        }
        result = method.invoke(obj, methodParaArr);
        return result;
    }

    // 初始化getter
    private void initGetters(Method[] methods) {
        List<FieldGetter> getters = new ArrayList<>();

        for (Method method : methods) {
            String methodName = method.getName();

            // 公开的实例方法&&get开头&&无参&&非桥接
            boolean isGetter = checkGetterMethod(methodName, method);
            if (isGetter) {
                // 反推field的名字
                String fieldName = OftenUtil.StringUtil.lowerFirstChar(methodName.substring(3));
                if (isValidPropertyName(fieldName)) {
                    getters.add(new FieldGetter(fieldName, method));
                }
            }
        }

        // 缓存类的getter函数列表
        getterMap.put(this.className, getters);
    }

    // 初始化字段Setter
    private void initSetters(Method[] methods) {
        List<FieldSetter> setters = new ArrayList<>();

        // 遍历类的可调用函数
        for (Method method : methods) {
            String methodName = method.getName();

            // 校验此方法是否合适（公开的实例方法&&set开头&&只有一个入参无返回值&&非桥接方法）
            boolean isSetter = checkSetterMethod(methodName, method);
            if (isSetter) {
                // 反推field的名字
                String fieldName = OftenUtil.StringUtil.lowerFirstChar(methodName.substring(3));
                if (isValidPropertyName(fieldName)) {
                    setters.add(new FieldSetter(fieldName, method));
                }
            }
        }

        // 缓存类的setter函数列表
        setterMap.put(this.className, setters);
    }

    private void initMethod(Method[] methods) {
        List<Method> publicInstanceMethods = new ArrayList<>();
        List<Method> publicStaticMethods = new ArrayList<>();

        for (Method method : methods) {
            String methodName = method.getName();

            // 过滤Object 类中的getXX 方法
            if (methodName.equals("getClass")) {
                continue;
            }

            int modifiers = method.getModifiers();
            // 公开的非静态
            if (!Modifier.isPublic(modifiers)) {
                continue;
            }

            // 非桥接
            if (method.isBridge()) {
                continue;
            }

            if (Modifier.isStatic(modifiers)) {
                publicStaticMethods.add(method);
            } else {
                publicInstanceMethods.add(method);
            }
        }

        // 缓存
        publicInstanceMethodMap.put(this.className, publicInstanceMethods);
        publicStaticMethodMap.put(this.className, publicStaticMethods);
    }

    // 获取字段Getter
    private Method getClsGetterMethod(String fieldName, Class<?>... parameterType) throws SecurityException, NoSuchMethodException {
        String methodName = String.format("get%s", OftenUtil.StringUtil.upperFirstChar(fieldName));
        // 获取field的setter，只要是用public修饰的setter、不管是自己的还是从父类继承的，都能取到
        return cls.getMethod(methodName, parameterType);
    }

    // 获取字段Setter
    private Method getClsSetterMethod(String fieldName, Class<?>... parameterType) throws SecurityException, NoSuchMethodException {
        String methodName = String.format("set%s", OftenUtil.StringUtil.upperFirstChar(fieldName));
        // 获取field的setter，只要是用public修饰的setter、不管是自己的还是从父类继承的，都能取到
        return cls.getMethod(methodName, parameterType);
    }

//    // 获取字段Setter，并更新缓存
//    private Method getAndCacheSetterMethod(String fieldName, Class<?>... parameterType) throws SecurityException,
//            NoSuchMethodException {
//        String methodName = String.format("set%s", OftenUtil.StringUtil.upperFirstChar(fieldName));
//        // 缓存
//        List<FieldSetter> fieldSetters = setterMap.get(this.className);
//        FieldSetter fieldSetter = fieldSetters.stream()
//                .filter(d -> methodName.equals(d.getFieldName()))
//                .findFirst()
//                .orElse(null);
//        if (fieldSetter == null) {
//            throw new IllegalStateException("未找到类" + this.className + "的方法：" + methodName);
//        }
//
//        // 获取field的setter，只要是用public修饰的setter、不管是自己的还是从父类继承的，都能取到
//        Method method = cls.getMethod(methodName, parameterType);
//        // 更新到缓存
//        fieldSetter.setMethod(method);
//        return method;
//    }

    // 校验Getter方法
    private static boolean checkGetterMethod(String methodName, Method method) {
        // 过滤Object 类中的getXX 方法
        if (methodName.equals("getClass")) {
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

        // 如果从名字推断是setter函数，添加到setter函数列表
        if (!methodName.startsWith("get")) {
            return false;
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

    // 是否有效的属性名
    private boolean isValidPropertyName(String name) {
        return !name.startsWith("$") && !"serialVersionUID".equals(name) && !"class".equals(name);
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
    static class FieldGetter {
        /**
         * 字段名
         */
        private String fieldName;
        /**
         * 方法
         */
        private Method method;

        /**
         * @param fieldName 字段名
         * @param method    字段对应的set方法
         */
        public FieldGetter(String fieldName, Method method) {
            this.fieldName = fieldName;
            this.method = method;
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
