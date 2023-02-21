package com.hxx.sbcommon.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-30 13:07:30
 **/
@Getter
@AllArgsConstructor
public enum ResultEnum {
    /**
     * 结果枚举，2001成功 2002参数校验失败 2003接口调用失败 2004无权访问
     */
    SUCCESS(2001, "成功"),
    PARAM_INVALID(2002, "参数校验失败"),
    COMMON_FAILED(2003, "接口调用失败"),
    FORBIDDEN(2004, "没有权限访问资源"),

    NO_TOKEN(2100, "没有TOKEN"),
    TOKEN_ERROR(2101, "TOKEN解析失败"),
    REFRESH_TOKEN_EXPIRED(2102, "REFRESH_TOKEN过期"),

    CURRENT_LIMITING(2200, "已被限流"),

    UNKNOWN(-1, "未知异常");

    private final Integer code;

    private final String name;

    private static Map<Integer, ResultEnum> codeLookup = new HashMap<>();

    static {
        for (ResultEnum val : values()) {
            codeLookup.put(val.code, val);
        }
    }

    /**
     * @param code
     * @return
     */
    public static ResultEnum getByCode(Integer code) {
        if (code == null) {
            return ResultEnum.UNKNOWN;
        }

        return codeLookup.getOrDefault(code, ResultEnum.UNKNOWN);
    }

    /**
     * @param code
     * @return
     */
    public static String getNameOrDefault(Integer code, String defaultName) {
        ResultEnum enumModel = getByCode(code);
        if (enumModel == ResultEnum.UNKNOWN) {
            return defaultName;
        }

        return enumModel.getName();
    }
}
