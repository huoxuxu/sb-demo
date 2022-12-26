package com.hxx.sbweb.common.global.model;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-04-19 16:22:13
 **/
@Data
public class RestResult<T> {
    private Class cls;
    private int code;
    private String message;
    private T data;

    public RestResult() {
    }
    public RestResult(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public RestResult(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }


    public static <T> RestResult<T> succcess(T data) {
        RestResult result = new RestResult();
        result.setData(data);
        return result;
    }

    public static RestResult fail(int code, String msg) {
        return new RestResult(code, msg);
    }

}