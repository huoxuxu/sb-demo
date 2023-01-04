package com.hxx.sbConsole.lock.distributed;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 分布式锁接口
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-03 17:31:00
 **/
public interface IDistributedLock {
    /**
     * 加锁
     *
     * @param key       锁Key
     * @param waitTime  尝试加锁，等待时间 (ms)
     * @param leaseTime 上锁后的失效时间 (ms)
     * @param success   锁成功执行的逻辑
     * @param fail      锁失败执行的逻辑 1加锁失败 2业务异常 9加锁异常
     * @return
     */
    <T> T lock(String key, int waitTime, int leaseTime, Supplier<T> success, Function<Integer, T> fail);
}
