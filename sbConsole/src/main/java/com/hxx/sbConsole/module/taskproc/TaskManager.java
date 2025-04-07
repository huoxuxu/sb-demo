package com.hxx.sbConsole.module.taskproc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskManager {
    public static final AtomicBoolean task1Lock = new AtomicBoolean(false); // 全局锁用于任务1的独占执行

    public static void main(String[] args) {
        // 初始化任务存储
        Map<String, TaskDetail> task1Map = new ConcurrentHashMap<>();
        Map<String, TaskDetail> task2Map = new ConcurrentHashMap<>();

        // 初始化任务1的10个明细
        for (int i = 1; i <= 10; i++) {
            task1Map.put("t1-" + i, new TaskDetail("t1-" + i));
        }

        // 初始化任务2的20个明细
        for (int i = 1; i <= 20; i++) {
            task2Map.put("t2-" + i, new TaskDetail("t2-" + i));
        }

        // 启动3个Worker
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 3; i++) {
            String workerId = "wk" + i;
            Worker worker = new Worker(workerId, task1Map, task2Map);
            executor.submit(worker);
        }

        // 关闭线程池（可选）
        // executor.shutdown();
    }
}
