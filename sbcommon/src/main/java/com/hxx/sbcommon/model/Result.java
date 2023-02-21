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

    /**
     * 未知异常
     *
     * @return
     */
    public static Result<?> failed() {
        return failed(ResultEnum.UNKNOWN);
    }

    /**
     * 未知异常
     *
     * @param message
     * @return
     */
    public static Result<?> failed(String message) {
        return failed(ResultEnum.UNKNOWN, message);
    }

    /**
     * 指定异常
     *
     * @param errorResult
     * @return
     */
    public static Result<?> failed(ResultEnum errorResult) {
        return failed(errorResult, errorResult.getName());
    }

    /**
     * 指定异常，自定义消息
     *
     * @param errorResult
     * @param message
     * @return
     */
    public static Result<?> failed(ResultEnum errorResult, String message) {
        return new Result<>(errorResult.getCode(), message, null);
    }

}