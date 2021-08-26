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
@RequestMapping("log")
public class Slf4jTestController extends BaseController {

    @RequestMapping("/w")
    public String w() {
        log.info("请求参数：{} {{}}{{++--}}",1,2,3,4);
        log.info("请求参数：\\{} {{}}{{++--}}",1,2,3,4);
        return ok(999);
    }

}
