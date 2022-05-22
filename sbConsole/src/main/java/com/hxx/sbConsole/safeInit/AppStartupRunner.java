package com.hxx.sbConsole.safeInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 与 CommandLineRunner接口类似, Spring boot 还提供另一个ApplicationRunner 接口来实现初始化逻辑。
 * 不同的地方在于 ApplicationRunner.run()方法接受的是封装好的ApplicationArguments参数对象，而不是简单的字符串参数。
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-22 13:00:06
 **/
@Slf4j
@Component
public class AppStartupRunner implements ApplicationRunner {
    public static int counter;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Application started with option names : {}",
                args.getOptionNames());
        log.info("Increment counter");
        counter++;
    }
}