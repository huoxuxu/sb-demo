package com.hxx.sbcommon.common.reflect;

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
        this.readablePropertyNames = (String[]) this.getMethods.keySet()
                .toArray(new String[this.getMethods.keySet()
                        .size()]);
        this.writeablePropertyNames = (String[]) this.setMethods.keySet()
                .toArray(new String[this.setMethods.keySet()
                        .size()]);
    }


    private void addGetMethods(Class<?> cls) {
        Map<String, List<Method>> conflictingGetters = new HashMap<>();
        Method[] methods = this.getClassMethods(cls);
        for (Method method : methods) {
            if (method.getParameterTypes().length <= 0) {
                String name = method.getName();
                if ((name.startsWith("get") && name.length() > 3) || (name.startsWith("is") && name.length() > 2)) {
                    name = PropertyNamer.methodToProperty(name);
                    this.addMethodConflict(conflictingGetters, name, method);
                }
            }
        }

        this.resolveGetterConflicts(conflictingGetters);
    }

    private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
        Iterator var2 = conflictingGetters.entrySet()
                .iterator();

        while (var2.hasNext()) {
            Map.Entry<String, List<Method>> entry = (Map.Entry) var2.next();
            Method winner = null;
            String propName = (String) entry.getKey();
            Iterator var6 = ((List) entry.getValue()).iterator();

            while (var6.hasNext()) {
                Method candidate = (Method) var6.next();
                if (winner == null) {
                    winner = candidate;
                } else {
                    Class<?> winnerType = winner.getReturnType();
                    Class<?> candidateType = candidate.getReturnType();
                    if (candidateType.equals(winnerType)) {
                        if (!Boolean.TYPE.equals(candidateType)) {
                            throw new ReflectionException("Illegal overloaded getter method with ambiguous type for property " + propName + " in class " + winner.getDeclaringClass() + ". This breaks the JavaBeans specification and can cause unpredictable results.");
                        }

                        if (candidate.getName()
                                .startsWith("is")) {
                            winner = candidate;
                        }
                    } else if (!candidateType.isAssignableFrom(winnerType)) {
                        if (!winnerType.isAssignableFrom(candidateType)) {
                            throw new ReflectionException("Illegal overloaded getter method with ambiguous type for property " + propName + " in class " + winner.getDeclaringClass() + ". This breaks the JavaBeans specification and can cause unpredictable results.");
                        }

                        winner = candidate;
                    }
                }
            }

            this.addGetMethod(propName, winner);
        }

    }

    private void addGetMethod(String name, Method method) {
        if (this.isValidPropertyName(name)) {
            this.getMethods.put(name, new MethodInvoker(method));
            Type returnType = TypeParameterResolver.resolveReturnType(method, this.type);
            this.getTypes.put(name, this.typeToClass(returnType));
        }

    }

    private void addSetMethods(Class<?> cls) {
        Map<String, List<Method>> conflictingSetters = new HashMap();
        Method[] methods = this.getClassMethods(cls);
        Method[] var4 = methods;
        int var5 = methods.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            String name = method.getName();
            if (name.startsWith("set") && name.length() > 3 && method.getParameterTypes().length == 1) {
                name = PropertyNamer.methodToProperty(name);
                this.addMethodConflict(conflictingSetters, name, method);
            }
        }

        this.resolveSetterConflicts(conflictingSetters);
    }

    private void resolveSetterConflicts(Map<String, List<Method>> conflictingSetters) {
        Iterator var2 = conflictingSetters.keySet()
                .iterator();

        while (var2.hasNext()) {
            String propName = (String) var2.next();
            List<Method> setters = (List) conflictingSetters.get(propName);
            Class<?> getterType = (Class) this.getTypes.get(propName);
            Method match = null;
            ReflectionException exception = null;
            Iterator var8 = setters.iterator();

            while (var8.hasNext()) {
                Method setter = (Method) var8.next();
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

            if (match == null) {
                throw exception;
            }

            this.addSetMethod(propName, match);
        }

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
        Field[] var3 = fields;
        int var4 = fields.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            if (canAccessPrivateMethods()) {
                try {
                    field.setAccessible(true);
                } catch (Exception var8) {
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

    private Method[] getClassMethods(Class<?> cls) {
        Map<String, Method> uniqueMethods = new HashMap();

        for (Class<?> currentClass = cls; currentClass != null && currentClass != Object.class; currentClass = currentClass.getSuperclass()) {
            this.addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());
            Class<?>[] interfaces = currentClass.getInterfaces();
            Class[] var5 = interfaces;
            int var6 = interfaces.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                Class<?> anInterface = var5[var7];
                this.addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }
        }

        Collection<Method> methods = uniqueMethods.values();
        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        Method[] var3 = methods;
        int var4 = methods.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Method currentMethod = var3[var5];
            if (!currentMethod.isBridge()) {
                String signature = this.getSignature(currentMethod);
                if (!uniqueMethods.containsKey(signature)) {
                    if (canAccessPrivateMethods()) {
                        try {
                            currentMethod.setAccessible(true);
                        } catch (Exception var9) {
                        }
                    }

                    uniqueMethods.put(signature, currentMethod);
                }
            }
        }

    }

    private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
        List<Method> list = conflictingMethods.get(name);
        if (list == null) {
            list = new ArrayList();
            conflictingMethods.put(name, list);
        }

        ((List) list).add(method);
    }

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

    private boolean isValidPropertyName(String name) {
        return !name.startsWith("$") && !"serialVersionUID".equals(name) && !"class".equals(name);
    }

    private String getSignature(Method method) {
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
                name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
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
