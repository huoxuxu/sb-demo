package com.hxx.sbweb.model.enums;

public enum ResultEnum {
    SUCCESS(200,"操作成功"),
    EXCEPTION(500, ""),
    NULL_ATTR(101,"属性为空");

    private int code;
    private String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
