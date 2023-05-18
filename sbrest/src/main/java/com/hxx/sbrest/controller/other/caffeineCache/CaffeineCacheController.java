package com.hxx.sbrest.controller.other.caffeineCache;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.hxx.sbrest.service.impl.caffeineCache.CaffeineCacheServiceImpl;
import com.hxx.sbrest.service.impl.caffeineCache.LocalCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hxx.sbrest.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-17 13:08:34
 **/
@RequestMapping("/caffeine")
@RestController
public class CaffeineCacheController {

    @Autowired
    private CaffeineCacheServiceImpl CaffeineCacheService;

    @RequestMapping("/demo1")
    public String demo1() {
        CaffeineCacheService.demo();
        System.out.println("ok");
        return "1";
    }

    @RequestMapping("/demo2")
    public String demo2() {
        try {
            User user1 = LocalCacheService.loadingCache.get(1L);
            System.out.println("第1次查user1：" + user1);

            Thread.sleep(10_000);
            // 缓存不存在，返回旧值，并触发缓存刷新
            user1 = LocalCacheService.loadingCache.get(1L);
            System.out.println("第2次查user1：" + user1);

            Thread.sleep(3_000);
            user1 = LocalCacheService.loadingCache.get(1L);
            System.out.println("第3次查user1：" + user1);

            System.out.println("ok");
            return "1";
        } catch (Exception ex) {
            System.out.println(ex + "");
        }
        return "0";
    }

    @RequestMapping("/demo3")
    public String demo3() {
        Long key = 998L;
        // 写入缓存
        LocalCacheService.loadingCache.put(key, new User(key));
        // 读取缓存
        // get 方法是以阻塞方式执行，即使多个线程同时请求该值也只会调用一次Function方法
        User u = LocalCacheService.loadingCache.get(key, k -> new User(9L));

        return "";
    }

    @RequestMapping("/demo4")
    public String demo4() {
        // 手动删除缓存
        // 任何时候，你都可以主动使缓存失效，而不用等待缓存被驱逐

        Long key = 998L;
        LoadingCache<Long, User> cache = LocalCacheService.loadingCache;
        // 单个key
        cache.invalidate(key);
        // 批量key
        List<Long> keys = new ArrayList<>();
        cache.invalidateAll(keys);
        // 所有key
        cache.invalidateAll();
        return "";
    }

    @RequestMapping("/demo5")
    public String demo5() {
        Cache<String, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .maximumSize(10)
                .build();

        String key = "hello";
        //如果一个key不存在，那么会进入指定的函数生成value
        Object value = cache.get(key, t -> setValue(key).apply(key));
        cache.put(key, value);

        //判断是否存在如果不存返回null
        Object ifPresent = cache.getIfPresent(key);
        //移除一个key
        cache.invalidate(key);

        System.out.println(value);
        return "";
    }

    @RequestMapping("/demo6")
    public String demo6() {
        String key = "hello";
        // 同步加载
        LoadingCache<String, Object> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(k -> setValue(key).apply(key));
        Object val = cache.get(key);
        System.out.println(val);
        return "";
    }

    @RequestMapping("/demo7")
    public String demo7() {
        String key = "hello";
        // 异步加载
        AsyncLoadingCache<String, Object> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .buildAsync(k -> setAsyncValue(key).get());

        Object val= cache.get(key);
        System.out.println(val);
        return "";
    }


    public Function<String, Object> setValue(String key) {
        return t -> key + "value";
    }

    public CompletableFuture<Object> setAsyncValue(String key){
        return CompletableFuture.supplyAsync(() -> {
            return key + "value";
        });
    }
}
