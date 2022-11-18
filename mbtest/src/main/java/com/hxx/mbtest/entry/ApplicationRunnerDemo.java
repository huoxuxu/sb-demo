package com.hxx.mbtest.entry;

import com.hxx.mbtest.service.impl.T1ServiceAutoTransactionImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-18 16:33:46
 **/
@Slf4j
@Component
public class ApplicationRunnerDemo implements ApplicationRunner {
    @Resource
    private T1ServiceAutoTransactionImpl t1ServiceAutoTransaction;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            t1ServiceAutoTransaction.insertM();
            System.out.println("ok1");
        } catch (Exception e) {
            System.out.println(e + "");
        }
    }

}
