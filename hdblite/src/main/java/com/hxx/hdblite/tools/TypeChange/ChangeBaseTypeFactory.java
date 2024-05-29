package com.hxx.hdblite.tools.TypeChange;

import com.hxx.hdblite.tools.TypeChange.Impls.ByteChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.Int16ChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.Int32ChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.Int64ChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.BigDecimalChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.DoubleChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.SingleChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.BooleanChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.LocalDateChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.LocalDateTimeChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.StringChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.DateChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.SQLDateChangeBaseType;
import com.hxx.hdblite.tools.TypeChange.Impls.TimestampChangeBaseType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
public final class ChangeBaseTypeFactory {
    //注册的字典
    private static Map<String, Class> mapDic = new HashMap<String, Class>();
    //基本数据类型
    private static List<Class> baseTypes = new ArrayList<Class>();

    static {
        // 基本数据类型
        baseTypes.add(int.class);
        baseTypes.add(long.class);

        baseTypes.add(double.class);

        baseTypes.add(boolean.class);
        baseTypes.add(LocalDateTime.class);

        baseTypes.add(String.class);


        mapDic.put(String.class.getTypeName(), (StringChangeBaseType.class));
        // 整型
        mapDic.put(byte.class.getTypeName(), (ByteChangeBaseType.class));
        mapDic.put(Byte.class.getTypeName(), (ByteChangeBaseType.class));
        mapDic.put(short.class.getTypeName(), (Int16ChangeBaseType.class));
        mapDic.put(Short.class.getTypeName(), (Int16ChangeBaseType.class));
        mapDic.put(int.class.getTypeName(), (Int32ChangeBaseType.class));
        mapDic.put(Integer.class.getTypeName(), (Int32ChangeBaseType.class));
        mapDic.put(long.class.getTypeName(), (Int64ChangeBaseType.class));
        mapDic.put(Long.class.getTypeName(), (Int64ChangeBaseType.class));


        // 浮点型
        mapDic.put(float.class.getTypeName(), (SingleChangeBaseType.class));
        mapDic.put(Float.class.getTypeName(), (SingleChangeBaseType.class));
        mapDic.put(double.class.getTypeName(), (DoubleChangeBaseType.class));
        mapDic.put(Double.class.getTypeName(), (DoubleChangeBaseType.class));
        mapDic.put(BigDecimal.class.getTypeName(), (BigDecimalChangeBaseType.class));


        // 布尔，日期
        mapDic.put(boolean.class.getTypeName(), (BooleanChangeBaseType.class));
        mapDic.put(Boolean.class.getTypeName(), (BooleanChangeBaseType.class));
        mapDic.put(LocalDateTime.class.getTypeName(), (LocalDateTimeChangeBaseType.class));
        mapDic.put(LocalDate.class.getTypeName(), (LocalDateChangeBaseType.class));


        // 其他
        mapDic.put(Timestamp.class.getTypeName(), (TimestampChangeBaseType.class));
        mapDic.put(Date.class.getTypeName(), (DateChangeBaseType.class));
        mapDic.put(java.sql.Date.class.getTypeName(), (SQLDateChangeBaseType.class));
    }

    /// <summary>
    /// 枚举类型无法使用,可以设计为int，在赋值时使用枚举值即可
    /// </summary>
    /// <param name="srcObj">待转换数据</param>
    /// <param name="tarType">目标类型</param>
    /// <returns></returns>
    public static Object Get(Object srcObj, Class tarType) throws Exception {
        String tarName = tarType.getTypeName();
        if (!baseTypes.stream().anyMatch(d -> d.getTypeName().equals(tarName)))
            throw new Exception("目标类型 " + tarName + " 不是基本数据类型！");

        Class srcType = srcObj.getClass();
        String srcTypeName = srcType.getTypeName();
        if (!mapDic.containsKey(srcTypeName))
            throw new Exception("未注册 " + srcTypeName + " 解析类!");

        Class changeType = mapDic.get(srcTypeName);
        IChangeBaseType ins = (IChangeBaseType) changeType.getConstructor(srcType).newInstance(srcObj);

        if (tarType == int.class) {
            return ins.ToInt32();
        } else if (tarType == long.class) {
            return ins.ToInt64();
        } else if (tarType == double.class) {
            return ins.ToDouble();
        } else if (tarType == boolean.class) {
            return ins.ToBoolean();
        } else if (tarType == LocalDateTime.class) {
            return ins.ToDateTime();
        }

        return ins.ToStr();
    }

    /// <summary>
    /// 注册类型解析
    /// </summary>
    /// <param name="typeFullName">类型全名</param>
    /// <param name="changeBaseType">类型转换类</param>
    public static void Regist(String typeFullName, Class changeBaseType) throws Exception {
        if (typeFullName == null || typeFullName.isEmpty()) throw new Exception("typeFullName 参数 不能为NULL");
        if (!IChangeBaseType.class.isAssignableFrom(changeBaseType))
            throw new Exception("注册类，必须继承自 IChangeBaseType ！");

        mapDic.put(typeFullName, changeBaseType);
    }

}
