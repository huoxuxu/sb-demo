package com.hxx.sbrest.controller;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.hxx.sbrest.controller.base.BaseController;
import com.hxx.sbrest.model.DataObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 14:57:52
 **/
@RestController
@RequestMapping("/cache")
public class CacheTestController extends BaseController {
    // 手动设置的Cache
    static Cache<String, DataObject> cache;
    // 同步设置Cache
    static LoadingCache<String, DataObject> cacheSync;
    // 异步设置Cache
    static AsyncLoadingCache<String, DataObject> cacheASync;

    static final String KEY = "A";

    static {
        cache = Caffeine.newBuilder()
                // 配置写入后到期策略
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();

        cacheSync = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .build(k -> DataObject.get("Data for " + k));

        cacheASync = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .buildAsync(k -> DataObject.get("Async Data for " + k));
    }

    /**
     * 手动缓存Demo
     * /cache/get
     *
     * @return
     */
    @RequestMapping("/get")
    public String get() {
        DataObject dataObject = cache.getIfPresent(KEY);
        if (dataObject == null) return ok("null");

        return ok(dataObject.show());
    }

    /**
     * 手动缓存Demo
     * /cache/set
     * @param val
     * @return
     */
    @RequestMapping("/set")
    public String set(String val) {
        cache.put(KEY, DataObject.get(val));

        return ok("set Cache " + val + " ok");
    }

    /**
     * 同步刷新缓存
     * @return
     */
    @RequestMapping("/getSync")
    public String getSync() {
        DataObject dataObject = cacheSync.get(KEY);
        if (dataObject == null) return ok("null");

        return ok(dataObject.show());
    }

    @RequestMapping("/getASync")
    public String getASync() {
        cacheASync.get(KEY).thenAccept(dataObject -> {
            String val=dataObject.show();
            System.out.println("============Caffeine=====================");
            System.out.println(val);
        });

        return ok("1");
    }

}
