package com.hxx.sbConsole.service.task;

import com.hxx.sbConsole.commons.config.ConfigurationPropertiesConfig;
import com.hxx.sbConsole.commons.config.PropertySourceConfig;
import com.hxx.sbConsole.service.UserService;
import com.hxx.sbConsole.service.impl.DemoAnnotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Spring调度，需要开启@EnableScheduling
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 10:47:15
 **/
@Slf4j
@Component
public class HostTask {
    @Autowired
    private ConfigurationPropertiesConfig config;
    @Autowired
    private PropertySourceConfig propConfig;
    @Autowired
    private UserService userService;

    @Autowired
    private DemoAnnotationService demoAnnotationService;

    // 需要开启@EnableScheduling
    //@Scheduled(initialDelay = 1 * 1000, fixedDelay = 1 * 1000)
    private void run() {
        System.out.println(LocalDateTime.now());
        System.out.println(config.getLastName());
        System.out.println(propConfig.getLastName());
        userService.selectById(1);
        demoAnnotationService.add();

    }

}
