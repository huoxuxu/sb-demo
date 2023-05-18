package com.hxx.sbrest.external.caffeineCache;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-17 12:52:18
 **/
@Component
public class CaffeineUtils {
    @Autowired
    Cache<String, Object> caffeineCache;

    /**
     * 添加或更新缓存
     *
     * @param key
     * @param value
     */
    public void putAndUpdateCache(String key, Object value) {
        caffeineCache.put(key, value);
    }


    /**
     * 获取对象缓存
     *
     * @param key
     * @return
     */
    public <T> T getObjCacheByKey(String key, Class<T> t) {
        caffeineCache.getIfPresent(key);
        return (T) caffeineCache.asMap().get(key);
    }

    /**
     * 根据key删除缓存
     *
     * @param key
     */
    public void removeCacheByKey(String key) {
        // 从缓存中删除
        caffeineCache.asMap().remove(key);
    }
}
