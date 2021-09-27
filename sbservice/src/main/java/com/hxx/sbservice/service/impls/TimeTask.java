package com.hxx.sbservice.service.impls;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-09-03 11:22:49
 **/
/**
 * 延时任务
 * 单链表，当有新的任务到来时，加到表尾
 */
public class TimeTask {
    public Runnable runnable;
    public int delayTimeSec;
    public TimeTask pre;
    public TimeTask next;

    public TimeTask(Runnable runnable, int delayTimeSec) {
        this.runnable = runnable;
        this.delayTimeSec = delayTimeSec;
    }
}
