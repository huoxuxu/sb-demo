package com.hxx.springcommon.config.dynamicdatasorce;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/*
    动态配置：
    java -jar -Dcustom.schema.name=huhu_db your-application.jar
*/

/**
 * 动态配置数据库
 */
@Configuration
public class DynamicDataSourceConfig {
    @Value("${custom.schema.name}")
    private String customSchemaName;

    @Bean
    public DataSource dataSource() {
        String dataSourceUrl = "jdbc:mysql://localhost:3306/" + customSchemaName;
        return DataSourceBuilder.create().url(dataSourceUrl).build();
    }

}
