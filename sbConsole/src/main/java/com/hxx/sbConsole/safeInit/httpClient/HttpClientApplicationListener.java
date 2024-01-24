package com.hxx.sbConsole.safeInit.httpClient;

import com.hxx.sbcommon.config.appconfig.HttpConfigProperties;
import com.hxx.sbcommon.config.appconfig.HttpConfigPropertiesProvider;
import com.hxx.sbcommon.config.appconfig.PropertyBinder;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-09 15:06:09
 **/
@Component
public class HttpClientApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        // 应用启动时读取配置
        PropertyBinder propertyBinder = new PropertyBinder(event.getEnvironment());
        BindResult<HttpConfigProperties> bindResult = propertyBinder.bind("app.http", HttpConfigProperties.class);
        HttpConfigPropertiesProvider.setHttpConfigProperties(bindResult.orElse(BeanUtils.instantiateClass(HttpConfigProperties.class)));
    }

}
