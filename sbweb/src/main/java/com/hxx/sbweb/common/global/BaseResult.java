package com.hxx.sbweb.common.global;

import lombok.Data;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-04-19 16:22:13
 **/
@Data
public class BaseResult<T> {
    private Class cls;
    private int code;
    private String msg;
    private T data;

    public BaseResult() {
    }
    public BaseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static <T> BaseResult<T> succcess(T data) {
        BaseResult result = new BaseResult();
        result.setData(data);
        return result;
    }

    public static BaseResult fail(int code, String msg) {
        return new BaseResult(code, msg);
    }

}