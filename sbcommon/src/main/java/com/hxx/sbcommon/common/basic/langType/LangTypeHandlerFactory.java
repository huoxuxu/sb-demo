package com.hxx.sbcommon.common.basic.langType;

import com.hxx.sbcommon.common.basic.langType.impl.*;
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
import com.hxx.sbcommon.common.basic.langType.impl.listLang.MapTypeHandler;
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
    // 自定义类型处理
    private static final Class<?> customTypeHandlerCls = CustomTypeHandler.class;
    // 枚举处理
    private static final Class<?> defaultEnumTypeHandler = EnumTypeHandler.class;

    static {
        typeHandlerMap.put(String.class, new StringTypeHandler());

        typeHandlerMap.put(Boolean.class, new BooleanTypeHandler());
        typeHandlerMap.put(Byte.class, new ByteTypeHandler());

        typeHandlerMap.put(Float.class, new FloatTypeHandler());
        typeHandlerMap.put(Double.class, new DoubleTypeHandler());
        typeHandlerMap.put(BigDecimal.class, new BigDecimalTypeHandler());

        typeHandlerMap.put(Short.class, new ShortTypeHandler());
        typeHandlerMap.put(Integer.class, new IntegerTypeHandler());
        typeHandlerMap.put(Long.class, new LongTypeHandler());

        typeHandlerMap.put(Date.class, new DateTypeHandler());
        typeHandlerMap.put(LocalDateTime.class, new LocalDateTimeTypeHandler());
        typeHandlerMap.put(LocalDate.class, new LocalDateTypeHandler());

        typeHandlerMap.put(Map.class, new MapTypeHandler());
        typeHandlerMap.put(HashMap.class, new MapTypeHandler());
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
     * 获取自定义类型处理器
     *
     * @return
     */
    public static Class<?> getCustomTypeHandlerCls() {
        return customTypeHandlerCls;
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
     * 获取类型处理器的实例
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

        if (ctorParamTypes.length > 0) {
            try {
                Constructor<?> c = typeHandlerClass.getConstructor(ctorParamTypes);
                return (LangTypeHandler) c.newInstance(ctorParams);
            } catch (Exception var6) {
                throw new TypeException("Failed invoking constructor for handler " + typeHandlerClass, var6);
            }
        }

        try {
            Constructor<?> c = typeHandlerClass.getConstructor();
            return (LangTypeHandler) c.newInstance();
        } catch (Exception var4) {
            throw new TypeException("Unable to find a usable constructor for " + typeHandlerClass, var4);
        }
    }

    /**
     * 将obj转为指定类型
     *
     * @param obj
     * @param tarCls
     * @return
     */
    public static Object convert(Object obj, Class<?> tarCls) {
        if (obj == null) {
            return null;
        }

        // 获取对应的处理方法
        LangTypeHandler<?> handler = LangTypeHandlerFactory.getTypeHandler(tarCls);
        if (handler == null) {
            // 是否枚举
            if (tarCls.isEnum()) {
                Class<?> enumTypeHandler = LangTypeHandlerFactory.getDefaultEnumTypeHandler();
                handler = LangTypeHandlerFactory.getInstance(enumTypeHandler, tarCls);
            } else {
                Class<?> customTypeHandler = LangTypeHandlerFactory.getCustomTypeHandlerCls();
                handler = LangTypeHandlerFactory.getInstance(customTypeHandler, tarCls);
            }
        }

        return handler.change(obj);
    }

}
