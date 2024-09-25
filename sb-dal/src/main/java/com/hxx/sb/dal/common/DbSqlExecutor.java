package com.hxx.sb.dal.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 不能用，仅示例
 */
public class DbSqlExecutor {
    /**
     * 定义连接对象
     * @Date 2024/5/8 13:20
     */

    private final Connection conn;

    /**
     * 构造方法,初始化连接对象
     * @Date 2024/5/8 13:20
     */
    public DbSqlExecutor(Connection conn) {
        this.conn = conn;
    }

    /**
     * 通用执行增删改操作方法
     * sql: sql语句
     * params: sql语句中占位符对应的参数
     * @Date 2024/5/8 13:23
     */
    public int executeUpdate(String sql,Object... params) throws SQLException {
        // 判断连接对象是否为空
        if (conn == null) {
            throw new RuntimeException("连接对象为空!!!");
        }

        // 判断sql语句是否为空
        if(sql == null || sql.isEmpty()) {
            throw new RuntimeException("sql语句为空!!!");
        }

        // 定义PreparedStatement对象,作用对sql语句进行预编译
        PreparedStatement ps = null;

        try {
            // 将sql进行预编译,方法中抛出SQL异常
            ps = conn.prepareStatement(sql);

            // 设置sql 中 ?号 占位符对应的参数
            // 比如 sql = "insert into user(name,age) values(?,?)"
//            setPreparedStatementParams(ps,params);

            // 执行sql语句，返回影响行数
            return ps.executeUpdate();
        } catch (SQLException e) {
            // 异常重抛,重抛成运行时异常
            throw new RuntimeException("执行sql语句失败!!!",e);
        } finally {
            // 关闭资源
            close(ps);
            close();
        }
    }

    /**
     * 设置sql预编译参数
     * @Date 2024/5/8 9:04
     */

    private void setPreparedStatement(PreparedStatement preparedStatement,Object... params)throws SQLException {
        // insert into user(id,name,age) values(?,?,?)
        // update user set name=?,age=? where id=?
        // 循环的边界为传递过来的params参数长度
        for (int i = 0; i < params.length; i++) {
            // 设置占位符?对应的值，占位符下标从1开始
            preparedStatement.setObject(i+1,params[i]);
        }
    }

    /**
     * 关闭PreparedStatement对象资源
     * @Date 2024/5/8 13:54
     */

    private void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                // 关闭PreparedStatement对象资源
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException("关闭PreparedStatement对象失败!!!",e);
            }
        }
    }

    /**
     * 关闭连接对象资源
     * @Date 2024/5/8 13:54
     */
    private void close() {
        if (conn != null ) {
            try {
                // 关闭连接对象资源
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("关闭conn对象资源失败!!!",e);
            }
        }
    }


    /**
     * 关闭ResultSet对象资源
     * @param rs 结果集对象
     */
    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                // 关闭ResultSet对象资源
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException("关闭ResultSet对象失败!!!",e);
            }
        }
    }
}
