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
            if (getMethod == null) continue;

            //执行get方法得到属性的值
            Object value = getMethod.invoke(bean);
            map.put(key, value);
        }

        return map;
    }

    /**
     * 将map中key对应的属性名赋值给对应的对象
     *
     * @param bean
     * @param sourceMap 数据来源
     * @param <T>
     * @throws Exception
     */
    public static <T> void copyToBean(T bean, Map<String, Object> sourceMap) throws Exception {
        //获取类的属性描述器
        BeanInfo beaninfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
        //获取类的属性集
        PropertyDescriptor[] props = beaninfo.getPropertyDescriptors();
        for (PropertyDescriptor prop : props) {
            //得到属性的name
            String key = prop.getName();
            Object val = sourceMap.getOrDefault(key, null);
            if (val == null) {
                continue;
            }

            Method writeMethod = prop.getWriteMethod();
            if (writeMethod != null) {
                writeMethod.invoke(bean, val);
            }
        }
    }

    /**
     * 复制src对象的属性到tar对象中
     *
     * @param src
     * @param tar
     * @param <T>
     * @throws Exception
     */
    public static <T> void copyTo(T src, T tar) throws Exception {
        Map<String, Object> srcMap = toMap(src);
        copyToBean(tar, srcMap);
    }

    /**
     * 复制src对象的属性值到tar对象对应的属性中
     *
     * @param src
     * @param tar
     * @param <T>
     * @throws Exception
     */
    public static <T> void copyToObj(T src, Object tar) throws Exception {
        Map<String, Object> srcMap = toMap(src);
        copyToBean(tar, srcMap);
    }

}
