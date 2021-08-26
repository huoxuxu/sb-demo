package com.hxx.mbtest.common;

import lombok.Data;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-07-01 9:42:17
 **/
@Data
public class WhereCase {
    private String condition;

    private Object value;

    private Object secondValue;

    // 只包含查询条件
    private boolean noValue;

    // 查询条件包含一个值
    private boolean singleValue;

    // 查询条件包含两个值
    private boolean betweenValue;

    // 查询条件包含多个值，value 类型为List<T>
    private boolean listValue;

    /**
     * @param condition
     * @param value
     * @param secValue
     */
    public WhereCase(String condition, Object value, Object secValue) {
        this.condition = condition;
        this.value = value;
        this.secondValue = secValue;
    }

    public static WhereCase createNoValue(String condition) {
        WhereCase whereCase = new WhereCase(condition, null, null);
        whereCase.setNoValue(true
        );
        return whereCase;
    }

    public static WhereCase createListValue(String condition, Object value) {
        WhereCase whereCase = new WhereCase(condition, value, null);
        whereCase.setListValue(true);
        return whereCase;
    }

    public static WhereCase createBetween(String condition, Object value, Object secondValue) {
        WhereCase whereCase = new WhereCase(condition, value, secondValue);
        whereCase.setBetweenValue(true);
        return whereCase;
    }

    public static WhereCase createSingleValue(String condition, Object value) {
        WhereCase whereCase = new WhereCase(condition, value, null);
        whereCase.setSingleValue(true);
        return whereCase;
    }


}
