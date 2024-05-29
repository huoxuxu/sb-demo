package com.hxx.hdblite.Pools;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * JDBC druid连接池帮助类
 */
public class JDBCUtils {
    /**
     * 定义数据源
     */
    private static DataSource ds;

    static {
        try {
            //加载配置文件
            InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties");
            Properties pro = new Properties();
            pro.load(is);

            //获取数据源
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据源
     * @return 返回数据源
     */
    public static DataSource getDataSource(){
        return ds;
    }

    /**
     * 获取连接对象
     * @return 返回连接对象
     * @throws SQLException  抛出的编译异常
     */
    public static Connection getConn() throws SQLException {
        return ds.getConnection();
    }

    /**
     *  关闭连接
     * @param stmt  sql执行对象
     * @param conn  数据库连接对象
     */
    public static void close(Statement stmt, Connection conn){
        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭资源的重载方法
     * @param rs    处理结果集的对象
     * @param stmt  执行sql语句的对象
     * @param conn  连接数据库的对象
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn){

        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
