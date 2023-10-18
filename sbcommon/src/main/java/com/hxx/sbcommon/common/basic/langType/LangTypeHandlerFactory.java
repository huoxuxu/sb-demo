package com.hxx.sbcommon.common.basic.langType;

import com.hxx.sbcommon.common.basic.langType.impl.booleanLang.BooleanTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.dateTimeLang.DateTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.dateTimeLang.LocalDateTimeTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.dateTimeLang.LocalDateTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.enumLang.EnumTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.floatLang.BigDecimalTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.floatLang.DoubleTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.floatLang.FloatTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.integerLang.ByteTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.integerLang.IntegerTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.integerLang.LongTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.integerLang.ShortTypeHandler;
import com.hxx.sbcommon.common.basic.langType.impl.stringLang.StringTypeHandler;
import org.apache.ibatis.type.TypeException;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 13:01:05
 **/
public class LangTypeHandlerFactory {
    private static final Map<Class<?>, LangTypeHandler<?>> typeHandlerMap = new HashMap<>();
    // 枚举
    private static final Class<?> defaultEnumTypeHandler = EnumTypeHandler.class;

    static {
        // 字符串
        typeHandlerMap.put(String.class, new StringTypeHandler());
        // 布尔型
        typeHandlerMap.put(Boolean.class, new BooleanTypeHandler());
        // 整型
        typeHandlerMap.put(Byte.class, new ByteTypeHandler());
        typeHandlerMap.put(Short.class, new ShortTypeHandler());
        typeHandlerMap.put(Integer.class, new IntegerTypeHandler());
        typeHandlerMap.put(Long.class, new LongTypeHandler());
        // 浮点型
        typeHandlerMap.put(Float.class, new FloatTypeHandler());
        typeHandlerMap.put(Double.class, new DoubleTypeHandler());
        typeHandlerMap.put(BigDecimal.class, new BigDecimalTypeHandler());
        // 日期
        typeHandlerMap.put(Date.class, new DateTypeHandler());
        typeHandlerMap.put(LocalDateTime.class, new LocalDateTimeTypeHandler());
        typeHandlerMap.put(LocalDate.class, new LocalDateTypeHandler());
    }

    /**
     * 将obj转为指定类型
     *
     * @param obj
     * @param tarCls 待转换为的类型
     * @return
     */
    public static Object convert(Object obj, Class<?> tarCls) {
        if (obj == null) return null;

        // 获取对应的处理方法
        LangTypeHandler<?> handler = LangTypeHandlerFactory.getTypeHandler(tarCls);
        if (handler == null) {
            // 是否枚举
            if (tarCls.isEnum()) {
                Class<?> enumTypeHandler = LangTypeHandlerFactory.getDefaultEnumTypeHandler();
                handler = LangTypeHandlerFactory.getInstance(enumTypeHandler, tarCls);
            } else {
                throw new IllegalArgumentException("未找到对应的类型处理器！" + tarCls.getName());
            }
        }

        return handler.change(obj);
    }

    /**
     * 获取类型对应的处理器，未找到返回null
     *
     * @param cls
     * @return
     */
    public static LangTypeHandler<?> getTypeHandler(Class<?> cls) {
        LangTypeHandler<?> typeHandler = typeHandlerMap.getOrDefault(cls, null);
        return typeHandler;
    }

    /**
     * 获取枚举类型处理器
     *
     * @return
     */
    public static Class<?> getDefaultEnumTypeHandler() {
        return defaultEnumTypeHandler;
    }

    /**
     * 注册类型处理器,
     * 已注册的不会重复注册
     *
     * @param newTypeCls     类型
     * @param newTypeHandler 对应的类型处理器
     */
    public static void RegisterTypeHandler(Class newTypeCls, LangTypeHandler newTypeHandler) {
        if (typeHandlerMap.containsKey(newTypeCls)) return;

        typeHandlerMap.put(newTypeCls, newTypeHandler);
    }

    /**
     * 取消注册类型处理器
     *
     * @param newTypeCls
     */
    public static void UnregisterTypeHandler(Class newTypeCls) {
        typeHandlerMap.remove(newTypeCls);
    }

    /**
     * 重新构造类型处理器的实例
     *
     * @param typeHandlerClass
     * @param ctorParams
     * @return
     */
    public static LangTypeHandler getInstance(Class<?> typeHandlerClass, Object... ctorParams) {
        if (ctorParams == null) {
            ctorParams = new Object[0];
        }
        // 获取构造参数类型
        Class<?>[] ctorParamTypes = new Class<?>[ctorParams.length];
        for (int i = 0; i < ctorParams.length; i++) {
            ctorParamTypes[i] = ctorParams[i].getClass();
        }

        // 带参构造
        if (ctorParamTypes.length > 0) {
            try {
                Constructor<?> c = typeHandlerClass.getConstructor(ctorParamTypes);
                return (LangTypeHandler) c.newInstance(ctorParams);
            } catch (Exception var6) {
                throw new TypeException("Failed invoking constructor for handler " + typeHandlerClass, var6);
            }
        }

        // 无参构造
        try {
            Constructor<?> c = typeHandlerClass.getConstructor();
            return (LangTypeHandler) c.newInstance();
        } catch (Exception var4) {
            throw new TypeException("Unable to find a usable constructor for " + typeHandlerClass, var4);
        }
    }

}
