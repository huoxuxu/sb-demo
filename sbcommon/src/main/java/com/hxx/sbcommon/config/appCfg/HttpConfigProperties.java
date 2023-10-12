package com.hxx.sbcommon.config.appCfg;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-09 15:06:40
 **/
@Data
public class HttpConfigProperties {

    /**
     * httpService扫描基路径
     */
    private String basePackage;

    /**
     * 连接池的最大连接数
     */
    private int maxConnectionSize = 200;
    /**
     * 单Route最大连接数
     */
    private int maxPerRouteSize = 200;
    /**
     * 连接的空闲多少秒后将被释放。
     */
    private long maxIdleSecond = 10L;

}