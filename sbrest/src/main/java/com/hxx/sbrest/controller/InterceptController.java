package com.hxx.sbrest.controller;

import com.hxx.sbrest.controller.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 14:57:52
 **/
@RestController
public class InterceptController extends BaseController {
    @RequestMapping("/index")
    public String index() {
        System.out.println("InterceptController/index");
        return ok("index");
    }

}
