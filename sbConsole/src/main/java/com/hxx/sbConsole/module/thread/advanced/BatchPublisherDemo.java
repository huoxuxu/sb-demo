package com.hxx.sbConsole.module.thread.advanced;

import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbcommon.common.thread.BaseBatchPublisher;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.LocalDateTime;
import java.util.Collection;

public class BatchPublisherDemo extends BaseBatchPublisher<String> {


    /**
     * @param thresholdValue 发送阈值
     * @param delayMs        发送最大延迟，ms
     */
    public BatchPublisherDemo(int thresholdValue, int delayMs) {
        super(thresholdValue, delayMs);
    }

    @Override
    public void consumerAsync(int source, Collection<String> ls) {
        System.out.println(LocalDateTime.now() + " source: " + source + " " + JsonUtil.toJSON(ls));
    }


    public static void main(String[] args) {
        try {
            BatchPublisherDemo publisher = new BatchPublisherDemo(5, 1000);
            for (int i = 0; i < 100; i++) {
                publisher.add(i + "");
                int randomVal = OftenUtil.RandomUtil.nextRandomVal(10, 400);
                System.out.println("random: " + randomVal + " val: " + i);
                Thread.sleep(randomVal);
            }

            System.out.println("ok!");
        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }
}
