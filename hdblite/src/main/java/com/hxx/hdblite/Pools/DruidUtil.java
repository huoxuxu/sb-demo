package com.hxx.hdblite.Pools;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.hxx.hdblite.DALPool;
import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.HConnInfo;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 单例
 * Druid 连接池使用
 */
public class DruidUtil {
    private static DruidUtil single = null;

    public static Map<String, DataSource> map = new HashMap<>();

    private DruidUtil() {
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static DruidUtil getInstance() {
        if (single == null) {
            synchronized (DruidUtil.class) {
                if (single == null) {
                    single = new DruidUtil();
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
        Properties prop = new Properties();
        HXXDBType dbType = db.getDBType();
        String cfgName = db.getConnCfgName();
        if (map.containsKey(cfgName)) {
            return;
        }


        String driverName = DALPool.GetDriveName(dbType);
        prop.setProperty("driver", driverName);
        prop.setProperty("url", db.getConnStr());
        //prop.setProperty("connectionProperties","useUnicode=true;characterEncoding=UTF8");

        if (dbType == HXXDBType.MySQL || dbType == HXXDBType.Oracle) {
            prop.setProperty("username", db.getUser());
            String passwd = db.getPassword();
            prop.setProperty("password", passwd);
        } else {
            throw new RuntimeException("连接池目前只支持Mysql、Oracle 数据库！");
        }

        //
        prop.setProperty("initialSize", "50");
        prop.setProperty("maxActive", "500");
        prop.setProperty("maxWait", "60000");
        prop.setProperty("maxIdleTime", "0");
        prop.setProperty("testOnBorrow", "true");
        prop.setProperty("testWhileIdle", "true");
        //检验连接是否有效的查询语句。如果数据库Driver支持ping()方法，则优先使用ping()方法进行检查，否则使用validationQuery查询进行检查。(Oracle jdbc Driver目前不支持ping方法)
        prop.setProperty("validationQuery", "select 1");
        try {
            DataSource druidDataSource = DruidDataSourceFactory.createDataSource(prop);
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
        DruidDataSource source1 = (DruidDataSource) source;
        if (source1 != null) {
            source1.close();
        }
        map.remove(cfgName);
    }


}
