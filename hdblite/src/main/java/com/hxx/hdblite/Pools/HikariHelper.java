package com.hxx.hdblite.Pools;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariHelper {
    public void Test() {
        HikariConfig config = new HikariConfig();
        //HikariCP将尝试通过仅基于jdbcUrl的DriverManager解析驱动程序，但对于一些较旧的驱动程序，还必须指定driverClassName
        config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/simpsons");
        config.setUsername("bart");
        config.setPassword("51mp50n");

//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        //池中维护的最小空闲连接数
        config.addDataSourceProperty("minimumIdle", "10");
        //池中最大连接数，包括闲置和使用中的连接
        config.addDataSourceProperty("maximumPoolSize", "100");

        HikariDataSource ds = new HikariDataSource(config);
    }

}
