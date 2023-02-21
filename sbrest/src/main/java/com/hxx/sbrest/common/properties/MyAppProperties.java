package com.hxx.sbrest.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-30 14:58:00
 **/
@Component
@ConfigurationProperties(prefix = "myapp")
public class MyAppProperties {

    private ValidateCodeProperties code = new ValidateCodeProperties();

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }
}
