package com.hxx.hdblite;

import com.hxx.hdblite.Models.DBBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * 数据库连接池
 */
public class DBPool {
    private DBBean dbBean;
    //创建连接总数
    private Integer totalActiveConnections = 0;
    //空闲链接
    private List<Connection> freeConnections = new Vector<>();
    //活动连接
    private List<Connection> activeConnections = new Vector<>();

    /**
     * @param dbBean
     */
    public DBPool(DBBean dbBean) {
        this.dbBean = dbBean;
        init();
    }

    /**
     * 初始化连接池
     */
    private void init() {
        try {
            Class.forName(dbBean.getDriverName());
            int min = dbBean.getInitConnection();
            for (int i = 0; i < min; i++) {
                Connection conn = newConnection();
                if (conn != null) {
                    freeConnections.add(conn);
                    totalActiveConnections++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取池中连接
     *
     * @return
     */
    public Connection get() {
        return getConnection();
    }

    /**
     * 还回连接池
     *
     * @param conn
     */
    public synchronized void release(Connection conn) {
        int max = dbBean.getMaxConnections();
        boolean flag = isValid(conn);
        if (flag) {
            if (freeConnections.size() <= max) {
                freeConnections.add(conn);
                activeConnections.remove(conn);

                totalActiveConnections--;

                this.notifyAll();//通知当前有可用连接
            } else {
                destroyConnection(conn);
            }
        }
    }

    /**
     * 销毁连接池
     *
     * @throws SQLException
     */
    public void destroyPool() throws SQLException {
        for (Connection conn : freeConnections) {
            destroyConnection(conn);
        }

        for (Connection conn : activeConnections) {
            destroyConnection(conn);
        }

        totalActiveConnections = 0;
    }

    /**
     * 校验连接池
     */
    public void checkPool() {
        System.out.println("空闲连接数" + freeConnections.size());
        System.out.println("活动连接数" + activeConnections.size());
        System.out.println("总连接数" + totalActiveConnections);
    }

    //辅助============

    /**
     * 创建新的连接
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private Connection newConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        if (dbBean != null) {
            Class.forName(dbBean.getDriverName());
            conn = DriverManager.getConnection(dbBean.getUrl(), dbBean.getUserName(), dbBean.getPassword());
        }
        return conn;
    }

    /**
     * 销毁指定连接
     *
     * @param conn
     */
    private void destroyConnection(Connection conn) {
        try {
            if (isValid(conn)) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 此连接是否为null或者已关闭
     *
     * @param conn
     * @return
     */
    private Boolean isValid(Connection conn) {
        try {
            if (conn == null || conn.isClosed()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 从线程池中获取连接，如果则自行创建
     *
     * @return
     */
    private Connection getConnection() {
        Connection conn = null;
        try {
            int max = this.dbBean.getMaxActiveConnections();

            if (totalActiveConnections < max) {//总连接数未超过最大值
                if (freeConnections.size() > 0) {
                    conn = freeConnections.get(0);
                    freeConnections.remove(0);
                } else {
                    conn = newConnection();
                }
            } else {//总连接数 超过最大值
                synchronized (this) {
                    this.wait(this.dbBean.getConnTimeOut());
                    conn = getConnection();
                }
            }
            if (isValid(conn)) {
                activeConnections.add(conn);
                totalActiveConnections++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


}
