package com.hxx.sbcommon.common;

import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: Bean 转换工具类.
 * @author: 龚哲
 * @Date: 2020/6/18 14:15
 */
@Component
public class BeanConvertMapUtils<T> {
    public Map beanToMap(T bean) throws Exception {
        Map map = new HashMap<>();
        //获取类的属性描述器
        BeanInfo beaninfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
        //获取类的属性集
        PropertyDescriptor[] pro = beaninfo.getPropertyDescriptors();
        for (PropertyDescriptor property : pro) {
            //得到属性的name
            String key = property.getName();
            Method get = property.getReadMethod();
            //执行get方法得到属性的值
            Object value = get.invoke(bean);
            map.put(key, value);
        }
        return map;
    }
}
