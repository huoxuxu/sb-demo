package com.hxx.mbtest.controller;

import com.hxx.mbtest.service.T1Service;
import com.hxx.mbtest.service.impl.T1ServiceImpl;
import com.hxx.mbtest.service.impl.db.trans.T1TransServiceImpl;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    private T1TransServiceImpl t1TransService;

    /**
     * http://localhost:8083/t1/run
     *
     * @return
     */
    @GetMapping("run")
    public String run() {
//        t1Service.Run();
        t1Service.updateBatch();

        return "ok";
    }

    /**
     * http://localhost:8083/t1/transaction
     *
     * @return
     */
    @GetMapping("transaction")
    public String transaction() {
        t1Service.TransactionDemo();

        return "ok";
    }

    /**
     * http://localhost:8083/t1/trans1
     *
     * @return
     */
    @GetMapping("trans1")
    public String transDemo(@RequestParam(defaultValue = "0") int flag) {
        try {
            boolean r = t1TransService.transDemo(flag);
            return r + "";
        } catch (Exception e) {
            return "出错了：" + ExceptionUtils.getStackTrace(e);
        }
    }

}
