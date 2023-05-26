package com.hxx.sbcommon.common.reflect;

import com.hxx.sbcommon.common.basic.OftenUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-25 18:09:25
 **/
@Slf4j
public class ReflectUseful {
    private Class<?> cls;

    private Method[] methods;
    private List<Method> publicInstanceMethods = new ArrayList<>();
    private List<Method> publicStaticMethods = new ArrayList<>();

    private List<String> fieldHasGetter = new ArrayList<>();
    private List<String> fieldHasSetter = new ArrayList<>();

    public ReflectUseful(Class<?> cls) {
        this.cls = cls;
        this.methods = cls.getMethods();
        initMethod();
    }

    public List<String> getFieldHasGetter() {
        return fieldHasGetter;
    }

    public List<String> getFieldHasSetter() {
        return fieldHasSetter;
    }

    /**
     * 是否有效的属性名
     *
     * @param name
     * @return
     */
    public static boolean isValidPropertyName(String name) {
        return !name.startsWith("$") && !"serialVersionUID".equals(name) && !"class".equals(name);
    }

    /**
     * 校验是否字段的Getter 方法
     *
     * @param methodName
     * @param method
     * @return
     */
    public static boolean checkGetterMethod(String methodName, Method method) {
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
    public static boolean checkSetterMethod(String methodName, Method method) {
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

    // 初始化
    private void initMethod() {
        if (methods == null || methods.length == 0) return;

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

                // 公开的实例方法&&get开头&&无参&&非桥接
                boolean isGetter = checkGetterMethod(methodName, method);
                if (isGetter) {
                    // 反推field的名字
                    String fieldName = OftenUtil.StringUtil.lowerFirstChar(methodName.substring(3));
                    if (isValidPropertyName(fieldName)) {
                        fieldHasGetter.add(fieldName);
                    }
                }

                // 校验此方法是否合适（公开的实例方法&&set开头&&只有一个入参无返回值&&非桥接方法）
                boolean isSetter = checkSetterMethod(methodName, method);
                if (isSetter) {
                    // 反推field的名字
                    String fieldName = OftenUtil.StringUtil.lowerFirstChar(methodName.substring(3));
                    if (isValidPropertyName(fieldName)) {
                        fieldHasSetter.add(fieldName);
                    }
                }
            }
        }
    }

}
