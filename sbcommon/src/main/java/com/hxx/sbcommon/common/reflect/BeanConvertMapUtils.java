package com.hxx.sbcommon.common.reflect;

import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: Bean 转换工具类.
 * @author:
 * @Date: 2020/6/18 14:15
 */
@Component
public class BeanConvertMapUtils {

    /**
     * bean转Map，k=属性名 v=属性值
     *
     * @param bean
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> Map<String, Object> toMap(T bean) throws Exception {
        Map<String, Object> map = new HashMap<>();

        //获取类的属性描述器
        BeanInfo beaninfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
        //获取类的属性集
        PropertyDescriptor[] pro = beaninfo.getPropertyDescriptors();
        for (PropertyDescriptor property : pro) {
            //得到属性的name
            String key = property.getName();
            Method getMethod = property.getReadMethod();
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
