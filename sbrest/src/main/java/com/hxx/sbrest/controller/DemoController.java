package com.hxx.sbrest.controller;

import com.hxx.sbcommon.common.io.json.JsonUtil;
import com.hxx.sbrest.model.T1Model;
import com.hxx.sbrest.service.BasicTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    private BasicTestService basicTestService;

    @RequestMapping("/getdata")
    public String getT1() {
        T1Model t1=new T1Model();
        {
            t1.setCode("key1");
            t1.setName("hxx");
            t1.setAge(30);
            t1.setScore(99.8);
            t1.setBirthday(LocalDateTime.now());
        }
        return JsonUtil.toJSON(t1);
    }

}
