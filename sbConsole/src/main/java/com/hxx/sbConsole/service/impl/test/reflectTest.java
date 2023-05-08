package com.hxx.sbConsole.service.impl.test;

import com.hxx.sbConsole.model.Employee;

import java.beans.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-05 10:28:07
 **/
public class reflectTest {
    public static void main(String[] args) {
        try{
            case0();
        }catch (Exception ex){
            System.out.println(ex+"");
        }
        System.out.println("ok!");
    }

    static void case0() throws IntrospectionException {
        Class<Employee> empCls = Employee.class;
        BeanInfo beanInfo = Introspector.getBeanInfo(empCls);
        BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
        // null
        BeanInfo[] additionalBeanInfo = beanInfo.getAdditionalBeanInfo();
        // 去除class等属性
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        // 包含wait notify等方法
        MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
        System.out.println("66");
    }
}
