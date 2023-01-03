package com.hxx.sbcommon.common.reflect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.util.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-30 8:59:55
 **/
@Slf4j
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

    // k=类名，v=所有Setter
    private Map<String,List<FieldSetter>>setterMap = new HashMap<>();

    public void initialize() {
        // 从缓存获取接口类的setter列表
        List<FieldSetter> setters = setterMap.get(this.getClass().getName());
        // 如果还没有缓存、初始化接口类的setter列表
        if (setters == null) {
            setters = this.initSetters();
        }
        // 遍历接口类的setter
        for (FieldSetter setter: setters) {
            // 用setter的名字（也就是字段的名字）检索键值对
            String fieldName = setter.getName();
            String fieldValue = nodes.get(fieldName);
            // 没有检索到键值对、或者键值对没有赋值，跳过
            if (StringUtils.isEmpty(fieldValue)) continue;
            try {
                Method method = setter.getMethod();
                // 用类反射方式调用setter
                method.invoke(this, fieldValue);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error("It's failed to initialize field: {}, reason: {}", fieldName, e);
                // 不能调用setter，可能是虚拟机回收了该子类的全部实例、入口地址变化，更新地址、再试一次
                try {
                    Method method = this.getSetter(fieldName);
                    setter.setMethod(method);
                    method.invoke(this, fieldValue);
                } catch (Exception e1) {
                    log.debug("It's failed to initialize field: {}, reason: {}", fieldName, e1);
                }
            } catch (Exception e) {
                log.error("It's failed to initialize field: {}, reason: {}", fieldName, e);
            }
        }
    }

    protected List<FieldSetter> initSetters() {
        String className = this.getClass().getName();
        List<FieldSetter> setters = new ArrayList<>();
        // 遍历类的可调用函数
        for (Method method: this.getClass().getMethods()) {
            String methodName = method.getName();
            // 如果从名字推断是setter函数，添加到setter函数列表
            if (methodName.startsWith("set")) {
                // 反推field的名字
                String fieldName = StringUtils.lowerFirstChar(methodName.substring(3));
                setters.add(new FieldSetter(fieldName, method));
            }
        }
        // 缓存类的setter函数列表
        setterMap.put(className, setters);
        // 返回可调用的setter函数列表
        return setters;
    }

    private Method getSetter(String fieldName) throws NoSuchMethodException, SecurityException {
        String methodName = String.format("set%s", StringUtils.upperFirstChar(fieldName));
        // 获取field的setter，只要是用public修饰的setter、不管是自己的还是从父类继承的，都能取到
        return this.getClass().getMethod(methodName, String.class);
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

    @lombok.Data
    static class FieldSetter {
        private String name;
        private Method method;

        public FieldSetter(String name, Method method) {
            this.name = name;
            this.method = method;
        }
    }

}
