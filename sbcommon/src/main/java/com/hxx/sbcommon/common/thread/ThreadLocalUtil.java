package com.hxx.sbcommon.common.thread;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;

import java.util.Map;

/**
 * 未验证
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-09 17:15:25
 **/
@Slf4j
public class ThreadLocalUtil {
    private static final ThreadLocal<Map<String, Object>> threadLocal = new NamedThreadLocal("sbcommon-threadlocal") {
        @Override
        protected Map<String, Object> initialValue() {
            return Maps.newHashMap();
        }
    };

    public static Map<String, Object> getThreadLocal() {
        return threadLocal.get();
    }

    public static <T> T get(String key, T defaultVal) {
        Map<String, Object> map = threadLocal.get();
        return (T) map.getOrDefault(key, defaultVal);
    }

    public static void set(String key, Object val) {
        Map<String, Object> map = threadLocal.get();
        map.put(key, val);
    }

    public static void set(Map<String, Object> map) {
        Map<String, Object> map1 = threadLocal.get();
        map1.putAll(map);
    }

    public static void remove() {
        threadLocal.remove();
    }


}
