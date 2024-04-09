package com.hxx.sbservice.service.jedis.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPubSub;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-04 15:08:16
 **/
@Slf4j
@Service
public class TwoSubscribe extends JedisPubSub {

    //接收到消息时执行
    @Override
    public void  onMessage(String channel, String message){
        System.out.println(" SecondJedisPubSub message is" + message);
    }

    //接收到模式消息时执行
    @Override
    public void onPMessage(String pattern, String channel, String message){
        System.out.println("SecondJedisPubSub pattern是"+pattern+"channel是"+channel + "message是" + message);
    }

    //取消订阅时执行
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels){
        System.out.println("SecondJedisPubSub 取消订阅"+channel);
    }

    //订阅时执行
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println("SecondJedisPubSub订阅成功");
    }

    //取消模式订阅时执行
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        System.out.println("SecondJedisPubSub 取消多订阅"+pattern);
    }

}
