package com.hxx.sbcommon.common.basic;

import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public class ComplexUtil {

    /**
     * 每10整数分钟执行，
     * 注意：
     * 精度最大支持10s
     * 可能执行多次，
     * 会在每10整数分钟的第50s后执行多次
     *
     * @param runAct
     * @param <T>
     */
    public static <T> void everyTenMinuteRun(Consumer<T> runAct) {
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        int second = now.getSecond();
        if (minute % 10 == 0 && second > 49) {
            runAct.accept(null);
        }
    }

}
