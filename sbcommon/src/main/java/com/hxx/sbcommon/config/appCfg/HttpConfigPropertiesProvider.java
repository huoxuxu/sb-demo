package com.hxx.sbcommon.config.appCfg;

import java.util.Optional;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-09 15:07:06
 **/
public class HttpConfigPropertiesProvider {
    private static HttpConfigProperties httpConfigProperties;

    public static void setHttpConfigProperties(HttpConfigProperties httpConfigProperties) {
        HttpConfigPropertiesProvider.httpConfigProperties = httpConfigProperties;
    }

    public static HttpConfigProperties getHttpConfigProperties() {
        return Optional.ofNullable(httpConfigProperties).orElseGet(()->new HttpConfigProperties());
    }
}
