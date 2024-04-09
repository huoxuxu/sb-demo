package com.hxx.sbcommon.common.thread;

import com.hxx.sbcommon.common.reflect.ReflectUseful;

import java.lang.reflect.Method;

/**
 * 通用Runnable
 * Runnable run = new CommonRunnable(bean, method);
 */
public class CommonRunnable implements Runnable {

    private final Object target;

    private final Method method;


    /**
     * Create a {@code CommonRunnable} for the given target instance,
     * calling the specified method.
     *
     * @param target the target instance to call the method on
     * @param method the target method to call
     */
    public CommonRunnable(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    /**
     * Create a {@code CommonRunnable} for the given target instance,
     * calling the specified method by name.
     *
     * @param target     the target instance to call the method on
     * @param methodName the name of the target method
     * @throws NoSuchMethodException if the specified method does not exist
     */
    public CommonRunnable(Object target, String methodName) throws NoSuchMethodException {
        this.target = target;
        this.method = target.getClass().getMethod(methodName);
    }


    /**
     * Return the target instance to call the method on.
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * Return the target method to call.
     */
    public Method getMethod() {
        return this.method;
    }


    @Override
    public void run() {
        try {
            ReflectUseful.makeAccessible(this.method);
            this.method.invoke(this.target);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        return this.method.getDeclaringClass().getName() + "." + this.method.getName();
    }

}
