package com.hxx.sbConsole.lock.distributed.impl;

import com.hxx.sbConsole.lock.distributed.IDistributedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-03 17:35:45
 **/
public class RedissonDistributedLock implements IDistributedLock {
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 加锁
     *
     * @param key       锁Key
     * @param waitTime  尝试加锁，等待时间 (ms)
     * @param leaseTime 上锁后的失效时间 (ms)
     * @param success   锁成功执行的逻辑
     * @param fail      锁失败执行的逻辑
     * @return
     */
    @Override
    public <T> T lock(String key, int waitTime, int leaseTime, Supplier<T> success, Function<Integer, T> fail) {
        RLock lock = null;
        // 是否加锁成功
        boolean locked = false;
        try {
            lock = redissonClient.getLock(key);
            locked = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if (locked) {
                // 加锁成功
                try {
                    return success.get();
                } catch (Exception e) {
                    // 业务异常
                    return fail.apply(2);
                }
            } else {
                // 未加锁成功
                return fail.apply(1);
            }
        } catch (InterruptedException e) {
            // 加锁或逻辑异常
            return fail.apply(9);
        } finally {
            if (lock != null && locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
