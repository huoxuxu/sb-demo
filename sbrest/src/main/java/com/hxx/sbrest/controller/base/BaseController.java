package com.hxx.sbrest.controller.base;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 14:58:39
 **/
public class BaseController {
    protected String ok(String data) {

        return data;
    }

    protected String ok(int data) {

        return data + "";
    }

}
