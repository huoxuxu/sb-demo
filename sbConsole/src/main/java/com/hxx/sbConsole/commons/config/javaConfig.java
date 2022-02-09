//package com.hxx.sbConsole.commons.config;
//
//import com.hxx.sbConsole.service.impl.FunctionService;
//import com.hxx.sbConsole.service.impl.UseFunctionService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 全注解方式加载 Spring 配置
// * @Configuration 注解用于定义一个配置类，相当于 Spring 的配置文件
// * 配置类中包含一个或多个被 @Bean 注解的方法，该方法相当于 Spring 配置文件中的 <bean> 标签定义的组件。

// * @Author: huoxuxu
// * @Description:
// * @Date: 2022-01-20 9:15:40
// **/
//@Configuration// @Configuration可理解为用spring的时候xml里面的<beans>标签
//public class javaConfig {
    /**
     * 与 <bean id="personService" class="PersonServiceImpl"></bean> 等价
     * 该方法返回值以组件的形式添加到容器中
     * 方法名是组件 id（相当于 <bean> 标签的属性 id)
     */
//    @Bean // @Bean可理解为用spring的时候xml里面的<bean>标签
//    public FunctionService functionService(){
//        return new FunctionService();
//    }
//    @Bean
//    public UseFunctionService useFunctionService(){
//        UseFunctionService useFunctionService=new UseFunctionService();
//        useFunctionService.setFunctionService(functionService());
//        return useFunctionService;
//    }
//
//}
