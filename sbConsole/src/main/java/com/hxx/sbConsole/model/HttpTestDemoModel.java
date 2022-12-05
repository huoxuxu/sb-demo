package com.hxx.sbConsole.model;

import java.time.LocalDateTime;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-05 13:00:15
 **/
@lombok.Data
public class HttpTestDemoModel {
    private int ok;
    private String msg;
    private DataDemoModel data;
    private String traceId;

    @lombok.Data
    public static class DataDemoModel{
        private String Method;
        private String Path;
        private String Query;
        private String QueryString;
        private String FormData;
        private String Body;
        private String Header;
    }
}
