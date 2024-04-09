package com.hxx.sbConsole.module.redis;

import com.hxx.sbservice.service.redis.RedisPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-04 16:19:36
 **/
@Component
public class RedisPubSubScheduled {

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisPublisher redisPublisher;

    //向redis消息队列index通道发布消息
    @Scheduled(fixedRate = 3000)
    public void sendMessage() {
//        stringRedisTemplate.convertAndSend("topic1", String.valueOf(Math.random()));
        redisPublisher.publish("topic1", String.valueOf(Math.random()));
    }

}
