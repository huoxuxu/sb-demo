package com.hxx.sbweb.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum LinePatternEnum implements EnumBase<LinePatternEnum> {
    // 分拣模式,0-小件分拣 1-大件分拣 2动态称
    SMALL(0, "小件分拣"), BIG(1, "大件分拣"), DWS(2, "动态称"), UNKNOWN(-1, "");

    private final Integer code;

    private final String name;

    private static Map<Integer, LinePatternEnum> codeLookup = new HashMap<>();

    static {
        for (LinePatternEnum val : values()) {
            codeLookup.put(val.code, val);
        }
    }

    /**
     * 获取所有的Code集合
     *
     * @return
     */
    public static int[] getCodes() {
        LinePatternEnum[] values = values();

        int[] arr = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            LinePatternEnum val = values[i];
            arr[i] = val.code;
        }
        return arr;
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
     * 获取枚举名称
     *
     * @param code
     * @return
     */
    public static String getNameOrDefault(Integer code, String defaultName) {
        LinePatternEnum enumVal = getByCode(code);
        if (enumVal == LinePatternEnum.UNKNOWN) {
            return defaultName;
        }

        return enumVal.getName();
    }

    /**
     * 获取枚举名称，未匹配到，返回默认值
     *
     * @param code
     * @param defaultStr
     * @return
     */
    public static String getEnumName(Integer code, String defaultStr) {
        LinePatternEnum enumVal = getByCode(code);
        if (enumVal == LinePatternEnum.UNKNOWN) {
            return defaultStr;
        }

        return enumVal.getName();
    }

    /**
     * 校验是否符合枚举
     *
     * @param val
     * @return
     */
    public static boolean check(Integer val) {
        if (val == null) {
            return false;
        }
        LinePatternEnum codeEnum = getByCode(val);
        return codeEnum != LinePatternEnum.UNKNOWN;
    }

    @Override
    public <T> int getCode1(T enumVal) {
        return 0;
    }
}