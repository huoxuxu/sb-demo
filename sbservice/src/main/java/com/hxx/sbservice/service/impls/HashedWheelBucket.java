package com.hxx.sbservice.service.impls;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-09-03 11:23:12
 **/
/**
 * 时间轮元素
 */
public class HashedWheelBucket {
    public TimeTask head;
    public TimeTask tail;
    public int size;
    public final Object lock = new Object();

    public boolean isEmpty() {
        return head == null && tail == null;
    }

    public boolean offer(TimeTask task) {
        if (task == null)
            throw new NullPointerException();
        if (isEmpty()) {
            head = tail = task;
        }else {
            tail.next = task;
            task.pre = tail;
            tail = task;
        }
        size++;
        return true;
    }

    public TimeTask poll() {
        if (isEmpty())
            return null;
        TimeTask t = head;
        if (head == tail) {
            head = tail = null;
        }else {
            head = t.next;
        }
        size--;
        return t;
    }
}
