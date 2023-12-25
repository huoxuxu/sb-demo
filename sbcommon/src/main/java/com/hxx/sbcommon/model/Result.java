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
    /**
     * 请求成功状态
     */
    private boolean state;
    /**
     * 状态码
     */
    private String statusCode;
    /**
     * 附加消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * @param statusCode
     * @param msg
     * @param data
     */
    public Result(String statusCode, String msg, T data) {
        this.statusCode = statusCode;
        this.message = msg;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultEnum.SUCCESS + "", ResultEnum.SUCCESS.getName(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultEnum.SUCCESS + "", message, data);
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
        return new Result<>(errorResult + "", message, null);
    }

}
