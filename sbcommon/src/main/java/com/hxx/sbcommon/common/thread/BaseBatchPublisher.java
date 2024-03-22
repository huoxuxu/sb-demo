package com.hxx.sbcommon.common.thread;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 批量发送者
 * 功能：
 * 本地缓冲累计指定数量后打包发送,
 * 缓冲区数据延时时间, 超时后立即发送
 */
@Slf4j
public abstract class BaseBatchPublisher<T> {
    private static final Executor customExecutor = ThreadPoolUtil.createCustomExecutor();

    private ScheduledExecutorService scheduleExeecutor;

    // 发送阈值
    private final int thresholdValue;

    // 最大延迟毫秒数
    private final int delayMs;

    // 上次出队时间
    private LocalDateTime preDequeueTime;

    private final Queue<T> queue = new ConcurrentLinkedQueue<>();

    /**
     * @param thresholdValue 发送阈值
     * @param delayMs        发送最大延迟，ms
     */
    public BaseBatchPublisher(int thresholdValue, int delayMs) {
        this.thresholdValue = thresholdValue;
        this.delayMs = delayMs;
        // 定时器
        this.scheduleExeecutor = ScheduledUtil.create("BaseBatchPublisher");
        ScheduledUtil.setRunMethod(scheduleExeecutor, 5, this::intervalPollQueue);
    }

    public void shutdown() {
        ScheduledUtil.shutdown(this.scheduleExeecutor);
    }

    // 入队
    public void add(T t) {
        queue.add(t);
        if (queue.size() > this.thresholdValue) {
            pollQueue(1);
        }
    }

    // 出队
    public void dequeue(int source, Collection<T> ls) {
        preDequeueTime = LocalDateTime.now();
        if (!CollectionUtils.isEmpty(ls)) {
            customExecutor.execute(() -> consumerAsync(source, ls));
        }
    }

    /**
     * 消费
     *
     * @param source 1add 2time
     * @param ls
     */
    public abstract void consumerAsync(int source, Collection<T> ls);

    // 出队
    private void pollQueue(int source) {
        List<T> ls = get();
        dequeue(source, ls);
    }

    // 定时出队
    private void intervalPollQueue() {
        if (preDequeueTime != null) {
            LocalDateTime now = LocalDateTime.now();
            // 上次提交时间大于当前，则置为当前，防止时间被往前调整了
            if (preDequeueTime.isAfter(now)) {
                preDequeueTime = now;
            }
            if (preDequeueTime.plus(delayMs, ChronoUnit.MILLIS).isAfter(now)) {
                return;
            }
        }
        pollQueue(2);
    }

    // 获取待消费的数据
    private List<T> get() {
        List<T> ls = new ArrayList<>();
        while (ls.size() < this.thresholdValue) {
            T p = queue.poll();
            if (p == null) {
                break;
            }
            ls.add(p);
        }
        return ls;
    }

}
