package com.hxx.sbrest.appInit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-06-16 10:23:21
 **/
@Component
public class MyApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
//    private static Map<String,UserService> userServiceMap = Maps.newHashMap();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        ApplicationContext applicationContext = event.getApplicationContext();
//        Map<String, UserService> beansOfType = applicationContext.getBeansOfType(UserService.class);
//        for (UserService userService : beansOfType.values()) {
//            System.out.println(userService);
//            userServiceMap.put(userService.getId(),userService);
//
//        }
//        System.out.println(userServiceMap);
    }

}
