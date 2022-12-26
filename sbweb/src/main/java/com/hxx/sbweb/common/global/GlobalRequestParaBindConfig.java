package com.hxx.sbweb.common.global;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-16 10:05:11
 **/
@ControllerAdvice
public class GlobalRequestParaBindConfig {

    @InitBinder("user")
    public void init1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user.");
    }

    @InitBinder("book")
    public void init2(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("book.");
    }

}
