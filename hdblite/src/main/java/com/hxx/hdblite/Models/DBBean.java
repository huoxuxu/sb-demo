package com.hxx.hdblite.Models;

/**
 *
 */
public class DBBean {
    /**
     * 数据库连接信息
     */
    private String driverName = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf-8";
    private String userName = "root";
    private String password = "123456";
    private String poolName = "testPool";
    //空闲时最小连接数
    private Integer minConnections = 3;
    //空闲时最大连接数
    private Integer maxConnections = 10;
    //初始化连接数量
    private Integer initConnection = 5;
    //重复获得连接的频率
    private Long connTimeOut = 1000L;
    //最大允许的连接数
    private Integer maxActiveConnections = 100;
    //连接超时时间
    private Long ConnectionTimeOut = 1000 * 60 * 20L;
    //是否获得当前连接
    private Boolean isCurrentConnection = true;
    //是否定时检查连接池
    private Boolean isCheckPool = true;
    //延迟多少时间后开始检查
    private Long lazyCheck = 1000 * 60 * 60L;
    //检查频率
    private Long periodCheck = 1000 * 60 * 60L;
    private static DBBean db;

    private DBBean() {
    }//使用单例对象

    public static DBBean getDB() {
        if (db == null) {
            db = new DBBean();
        }
        return db;
    }

    //get set===================
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Integer getMinConnections() {
        return minConnections;
    }

    public void setMinConnections(Integer minConnections) {
        this.minConnections = minConnections;
    }

    public Integer getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }

    public Integer getInitConnection() {
        return initConnection;
    }

    public void setInitConnection(Integer initConnection) {
        this.initConnection = initConnection;
    }

    public Long getConnTimeOut() {
        return connTimeOut;
    }

    public void setConnTimeOut(Long connTimeOut) {
        this.connTimeOut = connTimeOut;
    }

    public Integer getMaxActiveConnections() {
        return maxActiveConnections;
    }

    public void setMaxActiveConnections(Integer maxActiveConnections) {
        this.maxActiveConnections = maxActiveConnections;
    }

    public Long getConnectionTimeOut() {
        return ConnectionTimeOut;
    }

    public void setConnectionTimeOut(Long connectionTimeOut) {
        ConnectionTimeOut = connectionTimeOut;
    }

    public Boolean getCurrentConnection() {
        return isCurrentConnection;
    }

    public void setCurrentConnection(Boolean currentConnection) {
        isCurrentConnection = currentConnection;
    }

    public Boolean getCheckPool() {
        return isCheckPool;
    }

    public void setCheckPool(Boolean checkPool) {
        isCheckPool = checkPool;
    }

    public Long getLazyCheck() {
        return lazyCheck;
    }

    public void setLazyCheck(Long lazyCheck) {
        this.lazyCheck = lazyCheck;
    }

    public Long getPeriodCheck() {
        return periodCheck;
    }

    public void setPeriodCheck(Long periodCheck) {
        this.periodCheck = periodCheck;
    }
}
