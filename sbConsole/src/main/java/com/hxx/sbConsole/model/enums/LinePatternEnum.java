package com.hxx.sbConsole.model.enums;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;
import com.hxx.sbcommon.common.basic.langType.LangTypeHandlerFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-29 16:19:53
 **/
@Getter
@AllArgsConstructor
public enum LinePatternEnum {
    /**
     * 分拣模式,0-小件分拣 1-大件分拣 2动态称
     */
    SMALL(1, "小件分拣"),
    BIG(10, "大件分拣"),
    DWS(20, "动态称"),
    UNKNOWN(-1, "");

    private final Integer code;

    private final String name;

    private static Map<Integer, LinePatternEnum> codeLookup = new HashMap<>();

    static {
        for (LinePatternEnum val : values()) {
            codeLookup.put(val.code, val);
        }
    }

    /**
     * @param code
     * @return
     */
    public static LinePatternEnum getByCode(Integer code) {
        if (code == null) {
            return LinePatternEnum.UNKNOWN;
        }

        return codeLookup.getOrDefault(code, LinePatternEnum.UNKNOWN);
    }

    /**
     * @param code
     * @return
     */
    public static String getNameOrDefault(Integer code, String defaultName) {
        LinePatternEnum enumModel = getByCode(code);
        if (enumModel == LinePatternEnum.UNKNOWN) {
            return defaultName;
        }

        return enumModel.getName();
    }

}
