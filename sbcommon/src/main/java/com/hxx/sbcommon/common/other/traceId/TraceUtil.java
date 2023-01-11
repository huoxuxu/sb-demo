package com.hxx.sbcommon.common.other.traceId;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * 跟踪ID
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-10 13:56:34
 **/
@Slf4j
public class TraceUtil {
    public final static String TRACE_ID = "_traceId";

    public static String get() {
        return MDC.get(TRACE_ID);
    }

    public static void set(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    public static void clear() {
        MDC.remove(TRACE_ID);
    }

    public static String init() {
        String traceId = generateTraceId();
        set(traceId);
        return traceId;
    }


    private static String generateTraceId() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "");
    }

}
