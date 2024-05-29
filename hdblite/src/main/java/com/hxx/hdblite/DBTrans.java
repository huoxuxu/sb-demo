package com.hxx.hdblite;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务类
 */
public class DBTrans {
    private Connection conn;

    /**
     * 根据连接信息实例化事务类
     *
     * @param conn1
     */
    public DBTrans(Connection conn1) throws SQLException {
        this.conn = conn1;
        this.conn.setAutoCommit(false);//创建事务,其实是设置AutoCommit=false
    }

    /**
     * 提交事务
     *
     * @throws SQLException
     */
    public void Commit() throws SQLException {
        this.conn.commit();
        this.conn.setAutoCommit(true);
    }

    /**
     * 回滚事务
     *
     * @throws SQLException
     */
    public void Rollback() throws SQLException {
        this.conn.rollback();
        this.conn.setAutoCommit(true);
    }

}
