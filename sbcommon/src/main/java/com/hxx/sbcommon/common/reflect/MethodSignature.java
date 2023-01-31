package com.hxx.sbcommon.common.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/*
ParameterizedType:表示一种参数化的类型，比如Collection；
TypeVariable:表示泛型类型如T；
GenericArrayType：类型变量的数组类型；
WildcardType：一种通配符类型表达式，比如?, ? extends Number；
*/
/**
 * 方法签名
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-25 17:10:03
 **/
public class MethodSignature {
    // 是否公开
    private final boolean isPublic;
    // 是否静态
    private final boolean isStatic;
    // 返回类型
    private final Class<?> returnType;
    // 返回类型为void
    private final boolean returnsVoid;

    public MethodSignature(Class<?> mapperInterface, Method method) {
        int modifiers = method.getModifiers();
        this.isPublic= Modifier.isPublic(modifiers);
        this.isStatic=Modifier.isStatic(modifiers);

        {
            Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, mapperInterface);
            if (resolvedReturnType instanceof Class) {
                this.returnType = (Class)resolvedReturnType;
            } else if (resolvedReturnType instanceof ParameterizedType) {
                this.returnType = (Class)((ParameterizedType)resolvedReturnType).getRawType();
            } else {
                this.returnType = method.getReturnType();
            }

            this.returnsVoid = Void.TYPE.equals(this.returnType);
        }
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public Class<?> getReturnType() {
        return this.returnType;
    }

    public boolean returnsVoid() {
        return this.returnsVoid;
    }


    /**
     * 获取方法签名
     * java.lang.String#getName
     * void#setName:java.lang.String
     *
     * @param method
     * @return
     */
    public static String getMethodSignature(Method method) {
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

    private Integer getUniqueParamIndex(Method method, Class<?> paramType) {
        Integer index = null;
        Class<?>[] argTypes = method.getParameterTypes();

        for(int i = 0; i < argTypes.length; ++i) {
            if (paramType.isAssignableFrom(argTypes[i])) {
                if (index != null) {
                    throw new IllegalStateException(method.getName() + " cannot have multiple " + paramType.getSimpleName() +
                            " parameters");
                }

                index = i;
            }
        }

        return index;
    }

}
