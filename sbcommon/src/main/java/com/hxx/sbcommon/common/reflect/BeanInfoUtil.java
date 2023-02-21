package com.hxx.sbcommon.common.reflect;

import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
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

}
