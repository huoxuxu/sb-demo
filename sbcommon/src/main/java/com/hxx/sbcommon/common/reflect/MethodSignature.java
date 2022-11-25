package com.hxx.sbcommon.common.reflect;

import java.lang.reflect.Method;
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
    // 返回类型为void
    private final boolean returnsVoid;
    // 返回类型
    private final Class<?> returnType;

    public MethodSignature(Class<?> mapperInterface, Method method) {
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


    public Class<?> getReturnType() {
        return this.returnType;
    }

    public boolean returnsVoid() {
        return this.returnsVoid;
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
