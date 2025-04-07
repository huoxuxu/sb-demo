package com.hxx.sbConsole.module.taskproc;

import lombok.Data;

@Data
public class TaskDetail {
    private String taskId; // 任务ID
    private String status; // 状态：pending, running, completed
    private String owner;  // 当前执行的Worker ID
    private int version;   // 版本号（用于CAS）
    private long ttl;      // 过期时间戳（毫秒）

    public TaskDetail(String taskId) {
        this.taskId = taskId;
        this.status = "pending";
        this.version = 0;
        this.ttl = 0; // 初始为0表示未被抢占
    }
}
