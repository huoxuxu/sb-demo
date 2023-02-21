package com.hxx.sbConsole.commons.config.redis;

import com.ctrip.framework.apollo.ConfigService;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-16 15:12:19
 **/
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
//        com.ctrip.framework.apollo.Config appConfig = ConfigService.getAppConfig();
//        // 101.40.84.132:6401,101.40.84.133:6400
//        String nodes = appConfig.getProperty("spring.redis.cluster.nodes", null);
//        String password = appConfig.getProperty("spring.redis.password", null);
//
//        org.redisson.config.Config config = new org.redisson.config.Config();
//        config.useClusterServers()
//                .setPassword(password)
//                .addNodeAddress(nodes.split(","));
//        return Redisson.create(config);
        // 本地没有redis，暂时注释
        return null;
    }
}
