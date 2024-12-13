package com.hxx.sbcommon.common.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * caffeine 缓存基类
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-01-10 15:40:16
 **/
public class BaseCaffeineCache {
    private static final int CAPACITY = 16;
    private static final int MAX_SIZE = 500;
    private static final int FIXED_EXPIRE_SECOND = 30;

    private final Cache<String, Object> cache;

    protected BaseCaffeineCache() {
        this(CAPACITY, MAX_SIZE, FIXED_EXPIRE_SECOND);
    }

    /**
     * 初始化缓存基类
     *
     * @param capacity          缓存初始容量
     * @param maxSize           缓存最大个数
     * @param fixedExpireSecond 过期时间，秒
     */
    protected BaseCaffeineCache(int capacity, int maxSize, int fixedExpireSecond) {
        cache = Caffeine.newBuilder()
                //初始数量
                .initialCapacity(capacity)
                //最大条数
                .maximumSize(maxSize)
                // 最后一次写操作后经过指定时间过期
                .expireAfterWrite(fixedExpireSecond, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 获取缓存，不存在返回null
     *
     * @param key
     * @return
     */
    protected Object getIfPresentVal(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * 获取缓存，不存在则发起回调
     *
     * @param key
     * @param getFunc
     * @return
     */
    protected Object getVal(String key, Function<String, Object> getFunc) {
        return cache.get(key, getFunc);
    }

    /**
     * 获取缓存，不存在返回null
     *
     * @param key
     * @param <T>
     * @return
     */
    protected <T> T getIfPresent(String key) {
        return (T) getIfPresentVal(key);
    }

    /**
     * 获取缓存，不存在则回调
     *
     * @param key
     * @param getFunc
     * @param <T>
     * @return
     */
    protected <T> T get(String key, Function<String, T> getFunc) {
        return (T) cache.get(key, getFunc);
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param val
     */
    protected void set(String key, Object val) {
        cache.put(key, val);
    }

}


