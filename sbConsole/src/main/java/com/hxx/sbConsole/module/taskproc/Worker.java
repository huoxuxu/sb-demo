package com.hxx.sbConsole.module.taskproc;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.*;

public class Worker implements Runnable {
    private final String workerId;
    private final Map<String, TaskDetail> task1Map; // 任务1的任务明细
    private final Map<String, TaskDetail> task2Map; // 任务2的任务明细
    private final ExecutorService executorService; // 线程池用于并发抢占任务

    public Worker(String workerId,
                  Map<String, TaskDetail> task1Map,
                  Map<String, TaskDetail> task2Map) {
        this.workerId = workerId;
        this.task1Map = task1Map;
        this.task2Map = task2Map;
        this.executorService = Executors.newFixedThreadPool(2); // 创建固定大小的线程池
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 并发执行抢占任务1和任务2
                executorService.submit(this::acquireExclusiveTask);
                executorService.submit(this::acquireParallelTask);

                Thread.sleep(1000); // 每秒检查一次
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        executorService.shutdown(); // 关闭线程池
    }

    // 抢占任务1的明细（独占模式）
    private void acquireExclusiveTask() {
        if (!TaskManager.task1Lock.compareAndSet(false, true)) {
            return; // 如果全局锁已被占用，则跳过本次抢占
        }

        try {
            for (Map.Entry<String, TaskDetail> entry : task1Map.entrySet()) {
                TaskDetail current = entry.getValue();
                if (canAcquireExclusiveTask(current)) {
                    // 构造新状态
                    TaskDetail updated = new TaskDetail(entry.getKey());
                    updated.setStatus("running");
                    updated.setOwner(workerId);
                    updated.setVersion(current.getVersion() + 1);
                    updated.setTtl(System.currentTimeMillis() + 20000); // 20秒有效期

                    // CAS更新
                    if (task1Map.replace(entry.getKey(), current, updated)) {
                        System.out.println(LocalDateTime.now() + " " + workerId + " acquired task1: " + entry.getKey());
                        executeTask(entry.getKey()); // 模拟执行任务
                        completeTask(task1Map, entry.getKey()); // 标记任务完成
                        break; // 抢占成功后退出循环
                    }
                }
            }
        } finally {
            TaskManager.task1Lock.set(false); // 释放全局锁
        }
    }

    // 抢占任务2的明细（并行模式）
    private void acquireParallelTask() {
        for (Map.Entry<String, TaskDetail> entry : task2Map.entrySet()) {
            TaskDetail current = entry.getValue();
            if (canAcquireParallelTask(current)) {
                // 构造新状态
                TaskDetail updated = new TaskDetail(entry.getKey());
                updated.setStatus("running");
                updated.setOwner(workerId);
                updated.setVersion(current.getVersion() + 1);
                updated.setTtl(System.currentTimeMillis() + 20000); // 20秒有效期

                // CAS更新
                if (task2Map.replace(entry.getKey(), current, updated)) {
                    System.out.println(LocalDateTime.now() + " " + workerId + " acquired task2: " + entry.getKey());
                    executeTask(entry.getKey()); // 模拟执行任务
                    completeTask(task2Map, entry.getKey()); // 标记任务完成
                }
            }
        }
    }

    // 判断是否可抢占任务1的明细
    private boolean canAcquireExclusiveTask(TaskDetail detail) {
        return ("pending".equals(detail.getStatus()) ||
                ("running".equals(detail.getStatus()) && System.currentTimeMillis() > detail.getTtl()));
    }

    // 判断是否可抢占任务2的明细
    private boolean canAcquireParallelTask(TaskDetail detail) {
        return ("pending".equals(detail.getStatus()) ||
                ("running".equals(detail.getStatus()) && System.currentTimeMillis() > detail.getTtl()));
    }

    // 执行任务（模拟）
    private void executeTask(String taskId) {
        try {
            System.out.println(LocalDateTime.now() + " " + workerId + " is executing task: " + taskId);
            Thread.sleep(5000); // 模拟执行5秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 完成任务
    private void completeTask(Map<String, TaskDetail> taskMap, String taskId) {
        TaskDetail current = taskMap.get(taskId);
        if ("running".equals(current.getStatus())) {
            TaskDetail updated = new TaskDetail(taskId);
            updated.setStatus("completed");
            updated.setVersion(current.getVersion() + 1);
            taskMap.replace(taskId, current, updated);
        }
    }
}
