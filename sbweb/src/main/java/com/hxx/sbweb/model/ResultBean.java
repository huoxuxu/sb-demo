package com.hxx.sbweb.model;

import com.hxx.sbweb.model.enums.ResultEnum;

/**
 *
 * @param <T>
 */
public class ResultBean<T> {
    private int code;
    private String msg;
    private T data;

    public ResultBean(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultBean(ResultEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.data = data;
    }

    //
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
