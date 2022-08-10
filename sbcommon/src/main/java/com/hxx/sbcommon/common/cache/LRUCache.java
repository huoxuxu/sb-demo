package com.hxx.sbcommon.common.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = -5167631809472116969L;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int DEFAULT_MAX_CAPACITY = 1000;
    private final Lock lock;
    // 容量，注意：volatile
    private volatile int maxCapacity;

    public LRUCache() {
        this(1000);
    }

    public LRUCache(int maxCapacity) {
        super(16, 0.75F, true);
        this.lock = new ReentrantLock();
        this.maxCapacity = maxCapacity;
    }

    // 应从Map中删除最旧的条目时返回true，否则返回false
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > this.maxCapacity;
    }

    // 包含Key
    public boolean containsKey(Object key) {
        boolean var2;
        try {
            this.lock.lock();
            var2 = super.containsKey(key);
        } finally {
            this.lock.unlock();
        }

        return var2;
    }

    // 获取key对应的值
    public V get(Object key) {
        V var2;
        try {
            this.lock.lock();
            var2 = super.get(key);
        } finally {
            this.lock.unlock();
        }

        return var2;
    }

    // 设置kv
    public V put(K key, V value) {
        V var3;
        try {
            this.lock.lock();
            var3 = super.put(key, value);
        } finally {
            this.lock.unlock();
        }

        return var3;
    }

    // 移除指定key
    public V remove(Object key) {
        V var2;
        try {
            this.lock.lock();
            var2 = super.remove(key);
        } finally {
            this.lock.unlock();
        }

        return var2;
    }

    // 获取容量大小
    public int size() {
        int var1;
        try {
            this.lock.lock();
            var1 = super.size();
        } finally {
            this.lock.unlock();
        }

        return var1;
    }

    // 清理集合
    public void clear() {
        try {
            this.lock.lock();
            super.clear();
        } finally {
            this.lock.unlock();
        }

    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
