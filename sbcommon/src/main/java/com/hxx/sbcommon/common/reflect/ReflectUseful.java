package com.hxx.sbcommon.common.reflect;

import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.basic.text.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/*
  // 允许调用私有方法
  constructor.setAccessible(true);
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
    private List<Method> methods;
    // 构造函数
    private Constructor[] constructors;
    // 公开的实例方法
    private List<Method> publicInstanceMethods = new ArrayList<>();
    private List<Method> publicStaticMethods = new ArrayList<>();

    // 属性对应的字段
    private List<Field> propFields = new ArrayList<>();
    // 属性对应字段包含的特性集合
    private Map<String, Annotation[]> fieldAnnotationMap = new HashMap<>();

    // 包含Getter的字段
    private Set<String> fieldHasGetter = new HashSet<>();
    // 包含Setter的字段
    private Set<String> fieldHasSetter = new HashSet<>();

    // 属性集合
    private List<String> props = new ArrayList<>();

    public ReflectUseful(Class<?> currentCls) {
        this.currentCls = currentCls;

        // 获取类的所有属性
        this.fields = getFields(currentCls);
        if (CollectionUtils.isEmpty(fields)) return;

        this.methods = getMethods(currentCls);
        if (CollectionUtils.isEmpty(methods)) return;

        this.constructors = currentCls.getConstructors();
        initMethod(this.methods);
        initField(this.fields);
    }

    /**
     * 获取属性集合
     *
     * @return
     */
    public List<String> getProps() {
        return props;
    }

    // 初始化
    public static List<Field> getFields(Class<?> clazz) {
        List<Field> ls = new ArrayList<>();
        if (clazz == null || clazz == Object.class) return ls;

        Field[] fieldArr = clazz.getDeclaredFields();
        ls.addAll(Arrays.asList(fieldArr));

        List<Field> fs = getFields(clazz.getSuperclass());
        if (!CollectionUtils.isEmpty(fs)) {
            for (Field field : fs) {
                String name = field.getName();
                boolean flag = ls.stream().anyMatch(d -> name.equals(d.getName()));
                if (flag) continue;
                ls.add(field);
            }
        }
        return ls;
    }

    public static List<Method> getMethods(Class<?> clazz) {
        List<Method> ls = new ArrayList<>();
        if (clazz == null || clazz == Object.class) return ls;

        Method[] fieldArr = clazz.getDeclaredMethods();
        ls.addAll(Arrays.asList(fieldArr));

        List<Method> fs = getMethods(clazz.getSuperclass());
        if (!CollectionUtils.isEmpty(fs)) {
            for (Method item : fs) {
                String name = item.getName();
                boolean flag = ls.stream().anyMatch(d -> name.equals(d.getName()));
                if (flag) continue;

                ls.add(item);
            }
        }
        return ls;
    }

    private void initField(List<Field> fields) {
        if (CollectionUtils.isEmpty(fields)) return;

        for (Field field : fields) {
            String name = field.getName();

            if (fieldHasGetter.contains(name) && fieldHasSetter.contains(name)) {
                this.propFields.add(field);
                this.props.add(name);
                // 查询注解
                Annotation[] annotations = field.getAnnotations();
                fieldAnnotationMap.put(name, annotations);
            }
        }
    }

    private void initMethod(List<Method> methods) {
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

            if (Modifier.isFinal(modifiers)) continue;

            publicInstanceMethods.add(method);

            // 公开的实例方法&&get开头&&无参&&非桥接
            boolean isGetter = checkGetterMethod(methodName, method);
            if (isGetter) {
                // 反推field的名字
                String fieldName;
                if (method.getReturnType() == boolean.class) {
                    fieldName = StringUtil.lowerFirstChar(methodName.substring(2));
                } else {
                    fieldName = StringUtil.lowerFirstChar(methodName.substring(3));
                }
                if (isValidPropertyName(fieldName)) {
                    fieldHasGetter.add(fieldName);
                }
            }

            // 校验此方法是否合适（公开的实例方法&&set开头&&只有一个入参无返回值&&非桥接方法）
            boolean isSetter = checkSetterMethod(methodName, method);
            if (isSetter) {
                // 反推field的名字
                String fieldName = StringUtil.lowerFirstChar(methodName.substring(3));
                if (isValidPropertyName(fieldName)) {
                    fieldHasSetter.add(fieldName);
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
}
