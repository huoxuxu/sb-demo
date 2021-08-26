package com.hxx.sbweb.controller;

import com.hxx.sbweb.common.ResultHandler;
import com.hxx.sbweb.model.ResultBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/globalerr")
public class GlobalErrController {

    @RequestMapping("/ok")
    public ResultBean<List<Integer>> ok() {
        List<Integer>ls=new ArrayList<>();
        ls.add(99);
        ls.add(998);

        return ResultHandler.ok(ls);
    }

    @RequestMapping("/get")
    public List<Integer> get() {
        int a = 99 - 98 - 1;
        double b = 1 / a;
        return new ArrayList<>();
    }

}
