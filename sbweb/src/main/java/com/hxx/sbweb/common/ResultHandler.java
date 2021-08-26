package com.hxx.sbweb.common;

import com.hxx.sbweb.model.ResultBean;
import com.hxx.sbweb.model.enums.ResultEnum;

public class ResultHandler {
    /**
     * 成功时将object对象转化为ResultBean对象
     *
     * @param o
     * @return
     */
    public static ResultBean ok(Object o) {
        return new ResultBean(ResultEnum.SUCCESS, o);
    }

    /**
     * 使用枚举列举错误类型
     *
     * @param err
     * @return
     */
    public static ResultBean error(ResultEnum err) {
        return new ResultBean(err.getCode(), err.getMsg(), null);
    }

    /**
     *
     * @param err
     * @return
     */
    public static ResultBean error(String err) {
        return new ResultBean(ResultEnum.EXCEPTION.getCode(), err, null);
    }
}
