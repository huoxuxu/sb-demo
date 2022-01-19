package com.hxx.mbtest.controller;

import com.hxx.mbtest.service.T1Service;
import com.hxx.mbtest.service.impl.T1ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-09 12:48:08
 **/
@RestController
@RequestMapping("/t1")
public class T1Controller {
    @Autowired
    private T1ServiceImpl t1Service;

    @GetMapping("run")
    public String run() {
//        t1Service.Run();
        t1Service.updateBatch();

        return "ok";
    }

    @GetMapping("transaction")
    public String transaction() {
        t1Service.TransactionDemo();

        return "ok";
    }

}
