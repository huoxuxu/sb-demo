package com.hxx.sbservice.commons;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-30 9:59:42
 **/
public class YamlPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(resource.getResource());
        Properties properties = factoryBean.getObject();
        return name != null
                ? new PropertiesPropertySource(name, properties)
                : new PropertiesPropertySource(resource.getResource().getFilename(), properties);
    }
}
