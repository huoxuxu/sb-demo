package com.hxx.sbcommon.common.reflect;

import com.hxx.sbcommon.common.basic.OftenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-25 18:09:25
 **/
@Slf4j
public class ReflectUseful {
    private Class<?> cls;

    // 所有方法
    private Method[] methods;
    // 公开的实例方法
    private List<Method> publicInstanceMethods = new ArrayList<>();
    private List<Method> publicStaticMethods = new ArrayList<>();

    // 属性对应的字段
    private List<Field> fields = new ArrayList<>();
    // 属性对应字段包含的特性集合
    private Map<String, Annotation[]> fieldAnnotationMap = new HashMap<>();

    // 包含Getter的字段
    private Set<String> fieldHasGetter = new HashSet<>();
    // 包含Setter的字段
    private Set<String> fieldHasSetter = new HashSet<>();

    // 属性集合
    private List<String> props = new ArrayList<>();

    public ReflectUseful(Class<?> cls) {
        this.cls = cls;

        this.methods = cls.getMethods();
        initMethod();
        initField();
    }

    /**
     * 获取属性集合
     *
     * @return
     */
    public List<String> getProps() {
        return props;
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

    private void initField() {
        // 获取类的所有属性
        List<Field> fields = getFields(cls);
        if (CollectionUtils.isEmpty(fields)) return;

        for (Field field : fields) {
            String name = field.getName();

            if (fieldHasGetter.contains(name) && fieldHasSetter.contains(name)) {
                this.fields.add(field);
                this.props.add(name);
                // 查询注解
                Annotation[] annotations = field.getAnnotations();
                fieldAnnotationMap.put(name, annotations);
            }
        }
    }

    private void initMethod() {
        if (methods == null || methods.length == 0) return;

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

            if (Modifier.isStatic(modifiers)) {
                publicStaticMethods.add(method);
            } else {
                publicInstanceMethods.add(method);

                // 公开的实例方法&&get开头&&无参&&非桥接
                boolean isGetter = checkGetterMethod(methodName, method);
                if (isGetter) {
                    // 反推field的名字
                    String fieldName = "";
                    if (method.getReturnType() == boolean.class) {
                        fieldName = OftenUtil.StringUtil.lowerFirstChar(methodName.substring(2));
                    } else {
                        fieldName = OftenUtil.StringUtil.lowerFirstChar(methodName.substring(3));
                    }
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
