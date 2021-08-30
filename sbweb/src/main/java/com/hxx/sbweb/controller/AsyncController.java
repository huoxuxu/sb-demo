package com.hxx.sbweb.controller;

import com.hxx.sbservice.model.JdbcModel;
import com.hxx.sbservice.model.attr.Async.SyncService;
import com.hxx.sbservice.model.attr.Value.ValueTestConf;
import com.hxx.sbweb.common.JsonUtil;
import com.hxx.sbweb.common.ResultHandler;
import com.hxx.sbweb.domain.User;
import com.hxx.sbweb.model.ResultBean;
import com.hxx.sbweb.service.ConfigurationPropertiesTestService;
import com.hxx.sbweb.service.PropertySourceTestConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.Future;

/**
 * mybatis 注解方式示例
 */
@RestController
@RequestMapping("/attr")
public class AsyncController {
    @Autowired
    private SyncService syncService;


    /**
     * http://localhost:8082/attr/get?name=123
     *
     * @param name
     * @return
     */
    @RequestMapping("/get")
    public ResultBean<User> get(@RequestParam(value = "name") String name) {
        try {
            Future<String> result1 = syncService.method1("I");
            Future<String> result2 = syncService.method2("love");
            Future<String> result3 = syncService.method3("async");

            String str = result1.get();
            String str2 = result2.get();
            String str3 = result3.get();

            String result = str + str2 + str3;
            return ResultHandler.ok(result);
        } catch (Throwable ex) {

        }

        return ResultHandler.ok(1);
    }


}
