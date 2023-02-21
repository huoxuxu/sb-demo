package com.hxx.mbmtest.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-11 16:05:41
 **/
//表示这个类为一个配置类
@Configuration
// 配置mybatis的接口类放的地方
@MapperScan(basePackages = "com.hxx.mbtest.mapper.master", sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {
    // 设置mybatis的xml所在位置
    private String mapperXmlLocationPath="classpath*:mapper/master/*.xml";

    // 将这个对象放入Spring容器中
    @Bean(name = "masterDataSource")
    // 表示这个数据源是默认数据源
    @Primary
    // 读取application.properties中的配置参数映射成为一个对象
    // prefix表示参数的前缀
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource getDateSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "masterSqlSessionFactory")
    // 表示这个数据源是默认数据源
    @Primary
    // @Qualifier表示查找Spring容器中名字为test1DataSource的对象
    public SqlSessionFactory test1SqlSessionFactory(@Qualifier("masterDataSource") DataSource datasource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources(mapperXmlLocationPath));
        return bean.getObject();
    }

    @Bean("masterSqlSessionTemplate")
    // 表示这个数据源是默认数据源
    @Primary
    public SqlSessionTemplate test1sqlsessiontemplate(
            @Qualifier("masterSqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }


}
