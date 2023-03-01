package com.hxx.sbConsole.model.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-02-28 17:30:31
 **/
@Getter
@AllArgsConstructor
public enum DBTypeEnum {
    MySQL(0, "MySQL"),
    SQLite(1, "SQLite"),
    OracleDB(2, "OracleDB"),
    UNKNOWN(-1, "UNKNOWN");

    private final Integer code;

    private final String name;

    private static Map<Integer, DBTypeEnum> codeLookup = new HashMap<>();

    static {
        for (DBTypeEnum val : values()) {
            codeLookup.put(val.code, val);
        }
    }

    /**
     * @param code
     * @return
     */
    public static DBTypeEnum getByCode(Integer code) {
        if (code == null) {
            return DBTypeEnum.UNKNOWN;
        }

        return codeLookup.getOrDefault(code, DBTypeEnum.UNKNOWN);
    }

    /**
     * @param code
     * @return
     */
    public static String getNameOrDefault(Integer code, String defaultName) {
        DBTypeEnum enumModel = getByCode(code);
        if (enumModel == DBTypeEnum.UNKNOWN) {
            return defaultName;
        }

        return enumModel.getName();
    }
}
