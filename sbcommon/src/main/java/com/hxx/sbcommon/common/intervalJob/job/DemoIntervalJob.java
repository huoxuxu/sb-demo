package com.hxx.sbcommon.common.intervalJob.job;

import com.hxx.sbcommon.common.intervalJob.BaseIntervalJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-30 14:37:01
 **/
@Slf4j
@Component
public class DemoIntervalJob extends BaseIntervalJob {

    public DemoIntervalJob() {
        super(1);
    }

    /**
     * Job执行逻辑
     *
     * @param context job执行上下文
     */
    @Override
    public void process(IntervalJobContext context) {
        try {
            // 在run方法中编写任务逻辑
            log.info("[Job-" + jobCode + "] is running on thread " + Thread.currentThread().getName());
            // 模拟任务耗时
            Thread.sleep(2 * 1000);
        } catch (Exception e) {
            System.out.println(e + "");
        }
        throw new IllegalArgumentException("我是测试异常！");
    }
}
