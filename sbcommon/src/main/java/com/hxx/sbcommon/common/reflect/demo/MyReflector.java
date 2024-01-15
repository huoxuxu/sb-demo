package com.hxx.sbcommon.common.reflect.demo;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.apache.ibatis.reflection.invoker.GetFieldInvoker;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.invoker.MethodInvoker;
import org.apache.ibatis.reflection.invoker.SetFieldInvoker;

import java.lang.reflect.*;
import java.util.*;

/**
 * 反射工具类
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-09-19 16:00:30
 **/
public class MyReflector {
    // 类型
    private final Class<?> type;
    // 可读的属性名
    private final String[] readablePropertyNames;
    // 可写的属性名
    private final String[] writeablePropertyNames;
    private final Map<String, Invoker> setMethods = new HashMap<>();
    private final Map<String, Invoker> getMethods = new HashMap<>();
    /**
     * k=属性名，v=属性类型
     */
    private final Map<String, Class<?>> setTypes = new HashMap<>();
    /**
     * k=属性名，v=属性类型
     */
    private final Map<String, Class<?>> getTypes = new HashMap<>();

    public String[] getReadablePropertyNames() {
        return readablePropertyNames;
    }

    public String[] getWriteablePropertyNames() {
        return writeablePropertyNames;
    }

    public MyReflector(Class<?> clazz) {
        this.type = clazz;

        this.addGetMethods(clazz);
        this.addSetMethods(clazz);

        this.addFields(clazz);

        this.readablePropertyNames = this.getMethods.keySet()
                .toArray(new String[this.getMethods.keySet().size()]);
        this.writeablePropertyNames = this.setMethods.keySet()
                .toArray(new String[this.setMethods.keySet().size()]);
    }


    private void addGetMethods(Class<?> cls) {
        Map<String, List<Method>> getterMap = new HashMap<>();
        Method[] methods = this.getClassMethods(cls);
        for (Method method : methods) {
            if (method.getParameterTypes().length == 0) {
                String name = method.getName();
                if ((name.startsWith("get") && name.length() > 3) || (name.startsWith("is") && name.length() > 2)) {
                    name = PropertyNamer.methodToProperty(name);
                    this.addMethodToMap(getterMap, name, method);
                }
            }
        }

        this.resolveGetterConflicts(getterMap);
    }

    private void resolveGetterConflicts(Map<String, List<Method>> getterMap) {
        getterMap.forEach((propName, getters) -> {
            Method winner = null;
            for (Method getter : getters) {
                if (winner == null) {
                    winner = getter;
                } else {
                    Class<?> winnerType = winner.getReturnType();
                    Class<?> candidateType = getter.getReturnType();
                    if (candidateType.equals(winnerType)) {
                        if (!Boolean.TYPE.equals(candidateType)) {
                            throw new ReflectionException("Illegal overloaded getter method with ambiguous type for property " + propName + " in class " + winner.getDeclaringClass() + ". This breaks the JavaBeans specification and can cause unpredictable results.");
                        }

                        if (getter.getName()
                                .startsWith("is")) {
                            winner = getter;
                        }
                    } else if (!candidateType.isAssignableFrom(winnerType)) {
                        if (!winnerType.isAssignableFrom(candidateType)) {
                            throw new ReflectionException("Illegal overloaded getter method with ambiguous type for property " + propName + " in class " + winner.getDeclaringClass() + ". This breaks the JavaBeans specification and can cause unpredictable results.");
                        }

                        winner = getter;
                    }
                }
            }

            this.addGetMethod(propName, winner);
        });
    }

    private void addGetMethod(String name, Method method) {
        if (this.isValidPropertyName(name)) {
            this.getMethods.put(name, new MethodInvoker(method));
            Type returnType = TypeParameterResolver.resolveReturnType(method, this.type);
            this.getTypes.put(name, this.typeToClass(returnType));
        }

    }

    private void addSetMethods(Class<?> cls) {
        Map<String, List<Method>> settersMap = new HashMap<>();
        Method[] methods = this.getClassMethods(cls);

        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String name = method.getName();
            if (name.startsWith("set") && name.length() > 3 && method.getParameterTypes().length == 1) {
                name = PropertyNamer.methodToProperty(name);
                this.addMethodToMap(settersMap, name, method);
            }
        }

        this.resolveSetterConflicts(settersMap);
    }

    private void resolveSetterConflicts(Map<String, List<Method>> settersMap) {
        settersMap.forEach((propName, setters) -> {
            Class<?> getterType = this.getTypes.get(propName);
            Method match = null;
            ReflectionException exception = null;

            for (Method setter : setters) {
                Class<?> paramType = setter.getParameterTypes()[0];
                if (paramType.equals(getterType)) {
                    match = setter;
                    break;
                }

                if (exception == null) {
                    try {
                        match = this.pickBetterSetter(match, setter, propName);
                    } catch (ReflectionException var12) {
                        match = null;
                        exception = var12;
                    }
                }
            }

            if (match == null && exception != null) {
                throw exception;
            }

            this.addSetMethod(propName, match);
        });
    }

    private Method pickBetterSetter(Method setter1, Method setter2, String property) {
        if (setter1 == null) {
            return setter2;
        } else {
            Class<?> paramType1 = setter1.getParameterTypes()[0];
            Class<?> paramType2 = setter2.getParameterTypes()[0];
            if (paramType1.isAssignableFrom(paramType2)) {
                return setter2;
            } else if (paramType2.isAssignableFrom(paramType1)) {
                return setter1;
            } else {
                throw new ReflectionException("Ambiguous setters defined for property '" + property + "' in class '" + setter2.getDeclaringClass() + "' with types '" + paramType1.getName() + "' and '" + paramType2.getName() + "'.");
            }
        }
    }

    private void addSetMethod(String name, Method method) {
        if (this.isValidPropertyName(name)) {
            this.setMethods.put(name, new MethodInvoker(method));
            Type[] paramTypes = TypeParameterResolver.resolveParamTypes(method, this.type);
            this.setTypes.put(name, this.typeToClass(paramTypes[0]));
        }

    }

    private void addFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (int var5 = 0; var5 < fields.length; ++var5) {
            Field field = fields[var5];
            if (canAccessPrivateMethods()) {
                try {
                    field.setAccessible(true);
                } catch (Exception ignored) {
                }
            }

            if (field.isAccessible()) {
                if (!this.setMethods.containsKey(field.getName())) {
                    int modifiers = field.getModifiers();
                    if (!Modifier.isFinal(modifiers) || !Modifier.isStatic(modifiers)) {
                        this.addSetField(field);
                    }
                }

                if (!this.getMethods.containsKey(field.getName())) {
                    this.addGetField(field);
                }
            }
        }

        if (clazz.getSuperclass() != null) {
            this.addFields(clazz.getSuperclass());
        }

    }

    private void addSetField(Field field) {
        if (this.isValidPropertyName(field.getName())) {
            this.setMethods.put(field.getName(), new SetFieldInvoker(field));
            Type fieldType = TypeParameterResolver.resolveFieldType(field, this.type);
            this.setTypes.put(field.getName(), this.typeToClass(fieldType));
        }

    }

    private void addGetField(Field field) {
        if (this.isValidPropertyName(field.getName())) {
            this.getMethods.put(field.getName(), new GetFieldInvoker(field));
            Type fieldType = TypeParameterResolver.resolveFieldType(field, this.type);
            this.getTypes.put(field.getName(), this.typeToClass(fieldType));
        }

    }

    // 获取类型的所有方法,会获取基类的方法
    private Method[] getClassMethods(Class<?> cls) {
        Map<String, Method> uniqueMethods = new HashMap<>();

        for (Class<?> currentClass = cls; currentClass != null && currentClass != Object.class; currentClass = currentClass.getSuperclass()) {
            this.addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());
            Class<?>[] interfaces = currentClass.getInterfaces();

            for (Class<?> anInterface : interfaces) {
                this.addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }
        }

        Collection<Method> methods = uniqueMethods.values();
        return methods.toArray(new Method[methods.size()]);
    }

    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (int var5 = 0; var5 < methods.length; ++var5) {
            Method currentMethod = methods[var5];
            if (currentMethod.isBridge()) {
                continue;
            }

            String signature = this.getMethodSignature(currentMethod);
            if (uniqueMethods.containsKey(signature)) {
                continue;
            }

            if (canAccessPrivateMethods()) {
                try {
                    currentMethod.setAccessible(true);
                } catch (Exception ignored) {
                }
            }

            uniqueMethods.put(signature, currentMethod);
        }
    }

    // 加入Map中
    private void addMethodToMap(Map<String, List<Method>> methodMap, String name, Method method) {
        List<Method> item = methodMap.getOrDefault(name, new ArrayList<>());
        if (CollectionUtils.isEmpty(item)) {
            methodMap.put(name, item);
        }
        item.add(method);
    }

    // Type转Class
    private Class<?> typeToClass(Type src) {
        Class<?> result = null;
        if (src instanceof Class) {
            result = (Class) src;
        } else if (src instanceof ParameterizedType) {
            result = (Class) ((ParameterizedType) src).getRawType();
        } else if (src instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) src).getGenericComponentType();
            if (componentType instanceof Class) {
                result = Array.newInstance((Class) componentType, 0)
                        .getClass();
            } else {
                Class<?> componentClass = this.typeToClass(componentType);
                result = Array.newInstance(componentClass, 0)
                        .getClass();
            }
        }

        if (result == null) {
            result = Object.class;
        }

        return result;
    }

    // 是否有效的属性名
    private boolean isValidPropertyName(String name) {
        return !name.startsWith("$") && !"serialVersionUID".equals(name) && !"class".equals(name);
    }

    // 获取方法签名
    private String getMethodSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName())
                    .append('#');
        }

        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();

        for (int i = 0; i < parameters.length; ++i) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }

            sb.append(parameters[i].getName());
        }

        return sb.toString();
    }

    // 是否可访问私有方法
    private static boolean canAccessPrivateMethods() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }

            return true;
        } catch (SecurityException var1) {
            return false;
        }
    }

    public static class PropertyNamer {
        private PropertyNamer() {
        }

        public static String methodToProperty(String name) {
            if (name.startsWith("is")) {
                name = name.substring(2);
            } else {
                if (!name.startsWith("get") && !name.startsWith("set")) {
                    throw new ReflectionException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
                }

                name = name.substring(3);
            }

            if (name.length() == 1 || name.length() > 1 && !Character.isUpperCase(name.charAt(1))) {
                name = name.substring(0, 1)
                        .toLowerCase(Locale.ENGLISH) + name.substring(1);
            }

            return name;
        }

        public static boolean isProperty(String name) {
            return name.startsWith("get") || name.startsWith("set") || name.startsWith("is");
        }

        public static boolean isGetter(String name) {
            return name.startsWith("get") || name.startsWith("is");
        }

        public static boolean isSetter(String name) {
            return name.startsWith("set");
        }
    }
}
