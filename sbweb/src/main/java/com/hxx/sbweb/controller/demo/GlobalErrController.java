package com.hxx.sbweb.controller.demo;

import com.hxx.sbcommon.model.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试全局异常拦截
 */
@RestController
@RequestMapping("/globalerr")
public class GlobalErrController {

    @RequestMapping("/ok")
    public Result<List<Integer>> ok() {
        List<Integer> ls = new ArrayList<>();
        ls.add(99);
        ls.add(998);

        return Result.success(ls);
    }

    @RequestMapping("/get")
    public String get(int k, @RequestParam(defaultValue = "0") int timeout) throws Exception {
        // 超时
        if (timeout > 0) {
            Thread.sleep(timeout);
        }

        int a = 99 - 98 - 1;
        if (k == 1) {
            throw new Exception("123");
        } else if (k == 2) {
            throw new IOException("234");
        }

        return "ook";
    }

}
