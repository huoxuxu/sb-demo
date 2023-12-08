package com.hxx.sbservice.service.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * 消费消息
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-04 16:13:45
 **/
@Component
public class RedisSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String messageBody = new String(message.getBody());
        // 处理接收到的消息
        System.out.println("[REDIS-PUBSUB] Received message: " + messageBody + " from channel: " + channel);
    }

}
