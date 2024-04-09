package com.hxx.sbcommon.common.thread;

import com.google.common.collect.Maps;
import org.springframework.core.NamedThreadLocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * ThreadLocal-可关闭
 * 注意：
 * 同一ThreadLocal变量在父线程中被设置值后，在子线程中是获取不到的
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-08-08 16:01:09
 **/
public class ThreadLocalCloseable implements AutoCloseable {
    private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    @Override
    public void close() {
        remove();
    }

    /**
     * 获取本地变量的值
     *
     * @return
     */
    public Map<String, Object> get() {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        return map;
    }

    /**
     * 获取本地变量关联的值
     *
     * @param key
     * @return
     */
    public Object getValByKey(String key) {
        return get().getOrDefault(key, null);
    }

    /**
     * 设置本地变量的值
     *
     * @param key
     * @param val
     */
    public void set(String key, Object val) {
        Map<String, Object> map = get();
        map.put(key, val);
    }

    private static void demo() {
//        private static final
        ThreadLocal<Map<String, Object>> threadLocal = new NamedThreadLocal("sbcommon-threadlocal") {
            @Override
            protected Map<String, Object> initialValue() {
                return Maps.newHashMap();
            }
        };
    }

    /**
     * 清除本地变量
     */
    private void remove() {
        threadLocal.remove();
    }

}
