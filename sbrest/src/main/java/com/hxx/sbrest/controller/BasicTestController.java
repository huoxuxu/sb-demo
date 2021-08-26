package com.hxx.sbrest.controller;

import com.hxx.sbrest.controller.base.BaseController;
import com.hxx.sbrest.service.BasicTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
@RestController
@RequestMapping("bt")
public class BasicTestController extends BaseController {

    @Autowired
    private BasicTestService basicTestService;

    @RequestMapping("/switchTest")
    public String switchTest(String str) {
        log.info("请求参数：{} {{}}{{++--}}",str);
        String str1 = basicTestService.switchTest(str);
        return ok(str1);
    }

}
