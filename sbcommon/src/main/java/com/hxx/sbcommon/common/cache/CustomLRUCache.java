package com.hxx.sbcommon.common.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义的LRU缓存（线程不安全）
 * <p>
 * 如何保证线程安全：
 * 1.将普通的HashMap换成ConcurrentHashMap，双端链表换成ConcurrentLinkedQueue（此时链表内部维护的是key的访问顺序）；
 * 2.用可重入读写锁ReentrantReadWriteLock来保证put/get操作的线程安全性。
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-02-17 15:53:27
 **/
public class CustomLRUCache<T> {
    // 容量
    private final Map<Integer, ListNode<T>> container;
    // 头
    private final ListNode<T> head;
    // 尾
    private final ListNode<T> tail;
    // 容量
    private final int capacity;
    private int size;

    /**
     * 初始化容量500的缓存容器
     */
    public CustomLRUCache() {
        this(500);
    }

    /**
     * 初始化指定容量的缓存容器
     *
     * @param capacity 容量
     */
    public CustomLRUCache(int capacity) {
        this.container = new HashMap<>();

        this.head = this.tail = new ListNode<>();
        head.next = tail;
        tail.prev = head;

        this.capacity = capacity;
    }

    /**
     * 获取key对应的值，并将此key对应的数据写入头部
     *
     * @param key
     * @return
     */
    public T get(int key) {
        ListNode<T> data = container.get(key);
        if (data == null) {
            return null;
        }

        moveHead(data);
        return data.value;
    }

    /**
     * 写入
     *
     * @param key   键
     * @param value 值
     */
    public void put(int key, T value) {
        ListNode<T> data = container.get(key);
        // key 不存在
        if (data == null) {
            // 未超过容量
            if (size < capacity) {
                size++;
            }
            // 已超过容量
            else {
                ListNode<T> leastRecent = tail.prev;
                container.remove(leastRecent.key);
                deleteNode(leastRecent);
            }

            ListNode<T> newNode = new ListNode<>(key, value);
            insertNode(newNode);
            container.put(key, newNode);
        }
        // key 存在
        else {
            // 每次更新都重新写入头部
            data.value = value;
            moveHead(data);
        }
    }


    // 头插法
    private void insertNode(ListNode<T> node) {
        ListNode<T> headNext = head.next;
        head.next = node;
        node.prev = head;

        node.next = headNext;
        headNext.prev = node;
    }

    // 删除此节点
    private void deleteNode(ListNode<T> node) {
        ListNode<T> nPrev = node.prev,
                nNext = node.next;
        nPrev.next = nNext;
        nNext.prev = nPrev;
        node.prev = node.next = null;
    }

    // 移动到头部
    private void moveHead(ListNode<T> node) {
        deleteNode(node);
        insertNode(node);
    }

    /**
     * 节点
     */
    private static class ListNode<T> {
        // 键
        private int key;
        // 值
        private T value;
        // 上一个、下一个
        private ListNode<T> prev, next;

        public ListNode() {
        }

        public ListNode(int key, T value) {
            this.key = key;
            this.value = value;
        }
    }

}
