package com.hxx.sbcommon.common.timerJob;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class TimerJobContext implements Serializable {
    private static final long serialVersionUID = -4814674381214330014L;

    private String jobCode;
    private BaseTimerJob job;

    // 运行成功
    private boolean ok;

    // 开始运行时间
    private LocalDateTime runTime = LocalDateTime.now();
    // 运行完成时间
    private LocalDateTime completeTime;
    // 运行异常
    private Exception error;

    /**
     * 扩展数据
     */
    private Map<String, Object> extendData = new HashMap<>();

    public TimerJobContext() {
    }

    public TimerJobContext(String jobCode, BaseTimerJob job) {
        this.jobCode = jobCode;
        this.job = job;
    }

    /**
     * 设置成功
     */
    public void setSuccess() {
        setCompleted(true, null);
    }

    /**
     * 设置完成
     *
     * @param ok    是否成功
     * @param error 失败异常
     */
    public void setCompleted(boolean ok, Exception error) {
        this.ok = ok;
        this.error = error;
        this.completeTime = LocalDateTime.now();
    }
}
