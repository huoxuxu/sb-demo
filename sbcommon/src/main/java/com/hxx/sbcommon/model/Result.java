package com.hxx.sbcommon.model;

import com.hxx.sbcommon.model.enums.ResultEnum;
import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-04-19 16:22:13
 **/
@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    /**
     * @param code
     * @param msg
     * @param data
     */
    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getName(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), message, data);
    }

    public static Result<?> failed() {
        return new Result<>(ResultEnum.UNKNOWN.getCode(), ResultEnum.UNKNOWN.getName(), null);
    }

    public static Result<?> failed(String message) {
        return new Result<>(ResultEnum.UNKNOWN.getCode(), message, null);
    }

    public static Result<?> failed(ResultEnum errorResult) {
        return new Result<>(errorResult.getCode(), errorResult.getName(), null);
    }

}