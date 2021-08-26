package com.hxx.sbrest.common.starter;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-06 17:49:31
 **/
@Component
// 加上@Order注解后，执行顺序会根据数字从小到大执行
@Order(value=1)
public class StartLoader implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("#######################################系统初始化#######################################");
        loadSysParams();
        System.out.println("#######################################初始化完成#######################################");
    }

    public void loadSysParams(){
        System.out.println("【系统参数】加载中...");

        System.out.println("【系统参数】加载完成...");
    }

}
