package com.hxx.sbConsole.module.dynamicproxy.demo;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleDemo {

    @Autowired
    private Test02 test02;

    @Scheduled(fixedDelay = 3000)
    public void case1(){
        try{
            String str = test02.print();
            System.out.println("ok!");
        }catch (Exception ex){
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
        System.out.println("ok");
    }

}
