package com.hxx.sb.dal.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnUtil {

    /**
     * 数据库驱动
     */
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据据连接地址
     * jdbc:mysql://localhost:3306/数据库名称?serverTimezone=GMT%2B8"
     * jdbc:mysql://127.0.0.1:3306/demodb
     * ?zeroDateTimeBehavior=convertToNull&useUnicode=true&useJDBCCompliantTimezoneShift=true
     * &useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false&socketTimeout=60000
     */
    private static final String URL = "jdbc:mysql://localhost:3306/dbutils?serverTimezone=GMT%2B8";

    /**
     * 数据库用户名
     */
    private static final String USER = "root";

    /**
     * 数据库密码
     */
    private static final String PASSWORD = "1234";

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        try {
            // 注册JDBC驱动
            Class.forName(DRIVER);
            // 获取数据库连接
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("获取数据库连接失败", e);
        }
    }

}
