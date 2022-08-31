package com.hxx.sbcommon.common.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.util.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-30 8:59:55
 **/
public class ReflectorDemo {
    private final Class type;
//    private final String[] readablePropertyNames;
//    private final String[] writeablePropertyNames;
    private final Map<String, Class> setTypes = new HashMap<>();
    private final Map<String, Class> getTypes = new HashMap<>();
    private Constructor defaultConstructor;

    public ReflectorDemo(Class clazz) {
        this.type = clazz;
        this.addDefaultConstructor(clazz);

    }


    private void addDefaultConstructor(Class<?> clazz) {
        Constructor<?>[] consts = clazz.getDeclaredConstructors();
        Constructor[] var3 = consts;
        int var4 = consts.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Constructor<?> constructor = var3[var5];
            if (constructor.getParameterTypes().length == 0) {
                if (canAccessPrivateMethods()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (Exception var8) {
                    }
                }

                if (constructor.isAccessible()) {
                    this.defaultConstructor = constructor;
                }
            }
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

    private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
        List<Method> list = (List)conflictingMethods.get(name);
        if (list == null) {
            list = new ArrayList();
            conflictingMethods.put(name, list);
        }

        ((List)list).add(method);
    }


    private boolean isValidPropertyName(String name) {
        return !name.startsWith("$") && !"serialVersionUID".equals(name) && !"class".equals(name);
    }

    private Method[] getClassMethods(Class<?> currentClass) {
        Map<String, Method> uniqueMethods = new HashMap();

        for (; currentClass != null && currentClass != Object.class; currentClass = currentClass.getSuperclass()) {
            this.addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());
            Class<?>[] interfaces = currentClass.getInterfaces();

            for (int i = 0; i < interfaces.length; ++i) {
                Class<?> anInterface = interfaces[i];
                this.addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }
        }

        Collection<Method> methods = uniqueMethods.values();
        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (int i = 0; i < methods.length; ++i) {
            Method method = methods[i];
            if (method.isBridge()) {
                continue;
            }

            String signature = this.getMethodSignature(method);
            if (!uniqueMethods.containsKey(signature)) {
                if (canAccessPrivateMethods()) {
                    try {
                        method.setAccessible(true);
                    } catch (Exception var9) {
                    }
                }

                uniqueMethods.put(signature, method);
            }
        }
    }

    /**
     * 获取方法签名
     *
     * @param method
     * @return
     */
    private String getMethodSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName()).append('#');
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

}
