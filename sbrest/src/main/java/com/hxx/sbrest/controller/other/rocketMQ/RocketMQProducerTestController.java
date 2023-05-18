package com.hxx.sbrest.controller.other.rocketMQ;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
@RestController
@RequestMapping("rocketmq")
public class RocketMQProducerTestController {
    final static String PRODUCER_GROUP = "please_rename_unique_group_name_111222333";
    final static String NAME_SRV_ADDR = "localhost:9876";
    final static String TOPIC = "Jodie_topic_1023";
    final static String TAG = "TagA";

    @RequestMapping("/send")
    public String sendSync() {

        try {
            //Instantiate with a producer group name.
            DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
            // Specify name server addresses.
            producer.setNamesrvAddr(NAME_SRV_ADDR);
            //Launch the instance.
            producer.start();

            for (int i = 0; i < 100; i++) {
                byte[] body = ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET);
                //Create a message instance, specifying topic, tag and message body.
                Message msg = new Message(TOPIC, TAG, body);

                //Call send message to deliver message to one of brokers.
                SendResult sendResult = producer.send(msg);
                System.out.printf("%s%n", sendResult);
            }
            //Shut down once the producer instance is not longer in use.
            producer.shutdown();
        } catch (Exception ex) {
            log.error("生产异常！", ex);
        }
        return 1 + "";
    }

    @RequestMapping("/send2")
    public String sendASync() {
        try {
            //Instantiate with a producer group name.
            DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
            // Specify name server addresses.
            producer.setNamesrvAddr(NAME_SRV_ADDR);
            //Launch the instance.
            producer.start();
            producer.setRetryTimesWhenSendAsyncFailed(0);

            int messageCount = 100;
            final CountDownLatch countDownLatch = new CountDownLatch(messageCount);

            for (int i = 0; i < messageCount; i++) {
                try {
                    final int index = i;

                    byte[] body = ("Hello world " + i).getBytes(RemotingHelper.DEFAULT_CHARSET);
                    Message msg = new Message(TOPIC, TAG, "OrderID188", body);

                    producer.send(msg, new SendCallback() {
                        @Override
                        public void onSuccess(SendResult sendResult) {
                            countDownLatch.countDown();
                            System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                        }

                        @Override
                        public void onException(Throwable e) {
                            countDownLatch.countDown();
                            System.out.printf("%-10d Exception %s %n", index, e);
                            // e.printStackTrace();
                            log.error("生产异常！", e);
                        }
                    });
                } catch (Exception e) {
                    // e.printStackTrace();
                    log.error("生产异常！", e);
                }
            }
            countDownLatch.await(5, TimeUnit.SECONDS);
            producer.shutdown();
        } catch (Exception ex) {
            log.error("生产异常！", ex);
        }

        return 1 + "";
    }


}
