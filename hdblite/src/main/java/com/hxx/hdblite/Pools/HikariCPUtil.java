package com.hxx.hdblite.Pools;

import com.hxx.hdblite.DALPool;
import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.HConnInfo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 单例
 */
public class HikariCPUtil {
    private static HikariCPUtil single = null;

    public static Map<String, DataSource> map = new HashMap<>();

    private HikariCPUtil() {
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static HikariCPUtil getInstance() {
        if (single == null) {
            synchronized (HikariCPUtil.class) {
                if (single == null) {
                    single = new HikariCPUtil();
                }
            }
        }
        return single;
    }

    /**
     * @param cfgName
     * @return
     * @throws SQLException
     */
    public Connection getConnection(String cfgName) throws SQLException {
        DataSource source = map.get(cfgName);
        return source.getConnection();
    }

    /**
     * 添加连接信息
     *
     * @param db
     */
    public void add(HConnInfo db) {
        String cfgName = db.getConnCfgName();
        if (map.containsKey(cfgName)) {
            return;
        }

        HikariConfig config = new HikariConfig();
        HXXDBType dbType = db.getDBType();


        String driverName = DALPool.GetDriveName(dbType);
        config.setDriverClassName(driverName);
        config.setJdbcUrl(db.getConnStr());
        if (dbType == HXXDBType.MySQL || dbType == HXXDBType.Oracle) {
            String passwd = db.getPassword();
            config.setUsername(db.getUser());
            config.setPassword(passwd);
        } else {
            throw new RuntimeException("连接池目前只支持Mysql、Oracle 数据库！");
        }

        //连接允许在池中闲置的最长时间 ms 最小10000ms
        config.setIdleTimeout(10000);
        //池中维护的最小空闲连接数
        config.setMinimumIdle(3);
        //池中最大连接数，包括闲置和使用中的连接
        config.setMaximumPoolSize(1000);
        //验证连接有效性的超时时间(默认是5秒，最小不能小于250毫秒)
        config.setValidationTimeout(1000);
        //一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，
        //建议设置比数据库超时时长少30秒，参考MySQL wait_timeout参数（show variables like '%timeout%';）
        config.setMaxLifetime(120000);

        try {
            DataSource druidDataSource = new HikariDataSource(config);
            map.put(cfgName, druidDataSource);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("初始化创建连接池失败！" + cfgName);
        }
    }

    /**
     * @param db
     */
    public void remove(HConnInfo db) {
        String cfgName = db.getConnCfgName();
        if (!map.containsKey(cfgName)) {
            return;
        }

        DataSource source = map.get(cfgName);
        HikariDataSource source1 = (HikariDataSource) source;
        if (source1 != null) {
            source1.close();
        }
        map.remove(cfgName);
    }

}
