package com.hxx.mbtest.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-25 16:19:45
 **/
@Slf4j
public class MapperHelper {

    /**
     * 根据配置获取Mapper并执行其方法
     *
     * @throws IOException
     */
    public static void demo() throws IOException {
        String resource = "mybatis-config.xml";

        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(inputStream);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
//            // 常规方法
//            System.out.println(mapper.selectBlog(101));
            System.out.println(mapper.delBLog(1));
            // Object的方法
            System.out.println(mapper.hashCode());
//            // public default方法
//            System.out.println(mapper.defaultValue());
//            // 父接口中的方法
//            System.out.println(mapper.selectParent(101));
        }
    }

    public static void demo1() throws Exception {
        String PACK_PATH = "";
        String fileName = "com.x.x.xxMapper";
        Class cls = Class.forName(PACK_PATH + "." + fileName);


    }

    static List<Class> TYPE_ARRAY = new ArrayList<>();

    /**
     * @param c
     * @param o
     * @throws Exception
     */
    private void autoTestInvoke(Class c, Object o) throws Exception {
        Method[] declaredMethods = c.getDeclaredMethods();
        String fileName = c.getName()
                .substring(c.getName()
                        .lastIndexOf("."));

        // 给Mapper中的方法入参赋初始值，并执行方法
//        for (Method method : declaredMethods) {
//            List<Object> list = new ArrayList<>();
//            for (Class cls : method.getParameterTypes()) {
//                Object par = new Object();
//                if (TYPE_ARRAY.contains(cls)) {
//                    if (cls.equals(String.class)) {
//                        par = "1";
//                    } else {
//                        try {
//                            par = cls.newInstance();
//                            assignment(cls, par);
//                        } catch (InstantiationException e) {
//                            if (cls.isPrimitive()) {
//                                cls = primitiveClazz.get(cls.getName());
//                            }
//                            try {
//                                par = cls.getDeclaredConstructor(String.class)
//                                        .newInstance("1");
//                            } catch (NoSuchMethodException e1) {
//                                System.out.println(cls.getName() + e);
//                            }
//                        }
//                    }
//                } else if ("java.util.Map".equals(cls.getName())) {
//                    par = getMapData(c.getName() + "." + method.getName());
//                }
//
//                list.add(par);
//            }

//            try {
//                method.invoke(o, list.toArray());
//                invokeSuccess.add("Success: " + fileName + "." + method.getName());
//            } catch (Exception e) {
//                invokeFail.add("Error:" + method.getName() + " Error Info:" + e);
//            }
//    }
    }

    /**
     * blog
     */
    public interface BlogMapper {
        int delBLog(int id);
    }

    /*
    mybatis-config.xml
     <?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
    <!-- 数据源配置 -->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mydatabase"/>
                <property name="username" value="root"/>
                <property name="password" value="password"/>
            </dataSource>
        </environment>
    </environments>

    <!-- Mapper 接口扫描 -->
    <mappers>
        <mapper class="org.mybatis.example.UserMapper"/>
        <!-- 如果有多个 Mapper 接口，继续添加 -->
    </mappers>
</configuration>
    */
}
