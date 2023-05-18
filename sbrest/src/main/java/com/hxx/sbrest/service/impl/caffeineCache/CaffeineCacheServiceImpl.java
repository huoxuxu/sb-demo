package com.hxx.sbrest.service.impl.caffeineCache;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-17 12:57:13
 **/
@Slf4j
@Service
public class CaffeineCacheServiceImpl {

    @Autowired
    private Cache<String, Object> caffeineCache;


    public void demo() {
        String key = "111";

        Object value = caffeineCache.get(key, s -> key + "_" + "vvv");
        System.out.println(value);
    }


}
