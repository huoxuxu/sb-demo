package com.hxx.sbservice.service.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-04 16:12:10
 **/
@Component
public class RedisPublisher {
    /*
    spring.redis.host=localhost
    spring.redis.port=6379
    spring.redis.lettuce.pool.max-idle=8
    spring.redis.lettuce.pool.max-active=8
    spring.redis.lettuce.pool.max-wait=-1ms
    spring.redis.lettuce.pool.min-idle=0
    spring.redis.lettuce.shutdown-timeout=100ms
    */
    private final StringRedisTemplate redisTemplate;

    public RedisPublisher(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 发布消息
     *
     * @param channel
     * @param message
     */
    public void publish(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
