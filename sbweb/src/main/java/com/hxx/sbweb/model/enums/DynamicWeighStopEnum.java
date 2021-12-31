package com.hxx.sbweb.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-12-27 9:36:14
 **/
@Getter
@AllArgsConstructor
public enum DynamicWeighStopEnum {
    /**
     * 停秤类型，1：停秤成功。2：停秤失败。100：其他
     */
    OK(1, "停秤成功"),
    FAIL(2, "停秤失败"),
    UNKNOWN(100, "未知");

    /**
     * 编码
     */
    private final Integer code;

    /**
     * 名称
     */
    private final String name;

    /**
     * 查找Code对应枚举
     *
     * @param code
     * @return
     */
    public static DynamicWeighStopEnum getEnum(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }

        DynamicWeighStopEnum[] values = values();
        for (DynamicWeighStopEnum item : values) {
            if (code.equals(item.getCode())) {
                return item;
            }
        }
        return UNKNOWN;
    }

    /**
     * 查找Code对应Name
     *
     * @param code
     * @return
     */
    public static String getEnumName(Integer code) {
        DynamicWeighStopEnum enumIns = getEnum(code);
        if (enumIns == DynamicWeighStopEnum.UNKNOWN) {
            return String.valueOf(code);
        }

        return enumIns.getName();
    }

}
