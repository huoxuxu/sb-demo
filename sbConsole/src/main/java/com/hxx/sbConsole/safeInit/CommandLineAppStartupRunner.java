package com.hxx.sbConsole.safeInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 用来实现在应用启动后的逻辑控制
 * 这里的run方法会在Spring 上下文初始化完成后执行，同时会传入应用的启动参数
 * 对于多个CommandLineRunner的情况下可以使用@Order注解来控制它们的顺序
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-22 12:58:51
 **/
@Slf4j
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    public static int counter;

    @Override
    public void run(String... args) throws Exception {
        //上下文已初始化完成
        log.info("Increment counter");
        counter++;
    }
}
