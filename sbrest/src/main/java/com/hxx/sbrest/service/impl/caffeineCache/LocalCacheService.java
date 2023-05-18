package com.hxx.sbrest.service.impl.caffeineCache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.hxx.sbrest.model.User;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-18 15:26:22
 **/
public class LocalCacheService {
    /*
    initialCapacity 初始的缓存空间大小
    maximumSize 缓存的最大条数
    maximumWeight 缓存的最大权重
    expireAfterAccess 最后一次写入或访问后，经过固定时间过期
    expireAfterWrite 最后一次写入后，经过固定时间过期
    refreshAfterWrite 写入后，经过固定时间过期，下次访问返回旧值并触发刷新
    weakKeys 打开 key 的弱引用
    weakValues 打开 value 的弱引用
    softValues 打开 value 的软引用
    recordStats 缓存使用统计
    expireAfterWrite 和 expireAfterAccess 同时存在时，以 expireAfterWrite 为准。

    weakValues 和 softValues 不可以同时使用。

    maximumSize 和 maximumWeight 不可以同时使用。
    */
    public static LoadingCache<Long, User> loadingCache = Caffeine.newBuilder()
            // 初始的缓存空间大小
            .initialCapacity(5)
            // 缓存的最大条数
            .maximumSize(10)
            .expireAfterWrite(4, TimeUnit.SECONDS)
            .expireAfterAccess(10, TimeUnit.SECONDS)
            .refreshAfterWrite(6, TimeUnit.SECONDS)
            //.recordStats()
            //设置缓存的移除通知
            .removalListener(new RemovalListener<Long, User>() {
                @Override
                public void onRemoval(@Nullable Long key, @Nullable User user, @NonNull RemovalCause removalCause) {
                    System.out.printf("Key： %s ，值：%s was removed!原因 (%s) \n", key, user.getId(), removalCause);
                }
            })
            .build(id -> {
                System.out.println("缓存未命中，从数据库加载，用户id：" + id);
                return new User(id);
            });
}
