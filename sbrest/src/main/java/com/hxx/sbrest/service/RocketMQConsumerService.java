package com.hxx.sbrest.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-06-15 13:58:34
 **/
@Slf4j
@Service
public class RocketMQConsumerService {
    final static String CONSUMER_GROUP = "please_rename_unique_group_name";
    final static String NAME_SRV_ADDR = "localhost:9876";
    final static String TOPIC = "TopicTest";

    public static void consumer() {
        try {
            // Instantiate with specified consumer group name.
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP);

            // Specify name server addresses.
            consumer.setNamesrvAddr(NAME_SRV_ADDR);

            // Subscribe one more more topics to consume.
            consumer.subscribe(TOPIC, "*");
            // Register callback to execute on arrival of messages fetched from brokers.
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                                ConsumeConcurrentlyContext context) {
                    System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

            //Launch the consumer instance.
            consumer.start();

            System.out.printf("Consumer Started.%n");
        } catch (Exception ex) {
            log.error("消费异常", ex);
        }
    }

}
