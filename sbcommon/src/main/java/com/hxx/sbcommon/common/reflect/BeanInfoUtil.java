package com.hxx.sbcommon.common.reflect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-31 10:24:15
 **/
@Slf4j
public class BeanInfoUtil {
    /**
     * bean转Map，k=属性名 v=属性值
     *
     * @param bean
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> Map<String, Object> toMap(T bean) throws Exception {
        Map<String, Object> map = new HashMap<>();

        //获取类的属性描述器
        BeanInfo beaninfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
        //获取类的属性集
        PropertyDescriptor[] props = beaninfo.getPropertyDescriptors();
        for (PropertyDescriptor prop : props) {
            //得到属性的name
            String key = prop.getName();
            Method getMethod = prop.getReadMethod();
            if (getMethod == null) {
                continue;
            }

            //执行get方法得到属性的值
            Object value = getMethod.invoke(bean);
            map.put(key, value);
        }

        return map;
    }

    /**
     * 处理字段中的NULL设置为默认值
     *
     * @param bean
     * @param <T>
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> void procFieldNull(T bean) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        //获取类的属性描述器
        BeanInfo beaninfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
        //获取类的属性集
        PropertyDescriptor[] props = beaninfo.getPropertyDescriptors();
        for (PropertyDescriptor prop : props) {
            //得到属性的name
            String key = prop.getName();
            Method getMethod = prop.getReadMethod();
            if (getMethod == null) {
                continue;
            }
            Method writeMethod = prop.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }

            //执行get方法得到属性的值
            Object value = getMethod.invoke(bean);
            if (value == null) {
                Class<?> propType = prop.getPropertyType();
                if (propType == BigDecimal.class) {
                    value = BigDecimal.ZERO;
                } else if (propType == BigInteger.class) {
                    value = BigInteger.ZERO;
                }
                // 日期
                else if (propType == Date.class) {
                    value = new Date(0);
                } else if (propType == LocalDateTime.class) {
                    value = LocalDateTime.MIN;
                } else if (propType == LocalDate.class) {
                    value = LocalDate.MIN;
                }
                // boolean byte
                else if (propType == Boolean.class) {
                    value = false;
                } else if (propType == Byte.class) {
                    value = (byte) 0;
                }
                // 整型
                else if (propType == Short.class) {
                    value = (short) 0;
                } else if (propType == Integer.class) {
                    value = 0;
                } else if (propType == Long.class) {
                    value = (long) 0;
                }
                // 浮点型
                else if (propType == Float.class) {
                    value = (float) 0;
                } else if (propType == Double.class) {
                    value = (double) 0;
                }
                // 字符串
                else if (propType == String.class) {
                    value = "";
                } else if (propType == Character.class) {
                    value = Character.MIN_VALUE;
                } else {
                    throw new NotImplementedException("不支持的数据类型：" + key + " （" + propType.getTypeName() + "）");
                }
                // 枚举？
                writeMethod.invoke(bean, value);
            }
        }
    }

}
