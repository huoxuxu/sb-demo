package com.hxx.sbConsole.service.common;

/**
 * 租户测试
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-04-25 17:30:26
 **/
public class LesseeServiceImpl {

    private LesseeConfig lesseeConfig;
    private DemoDbContext demoContext;

    public LesseeServiceImpl(LesseeConfig lesseeConfig) {
        this.lesseeConfig = lesseeConfig;
        this.demoContext = new DemoDbContext(this.lesseeConfig.dbName);
    }

    public int del() {
        return this.demoContext.del();
    }


    public static class LesseeConfig {
        private String dbName;
        public LesseeConfig(){}

        public LesseeConfig(String dbName) {
            this.dbName = dbName;
        }
    }

    public static class DemoDbContext {
        private String dbName;

        public DemoDbContext(String dbName) {
            this.dbName = dbName;
        }

        public int del() {
            System.out.println("准备访问" + dbName);
            System.out.println("执行del");
            return 1;
        }
    }
}
