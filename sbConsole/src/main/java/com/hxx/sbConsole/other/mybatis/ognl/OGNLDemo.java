package com.hxx.sbConsole.other.mybatis.ognl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-10 16:04:02
 **/
@Slf4j
public class OGNLDemo {
    public static void main(String[] args) throws Exception {
        try {
            demo();
        } catch (Exception ex) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(ex));
        }
    }

    public static void demo() throws OgnlException {
        Map<String, Object> context = new HashMap<>();
        context.put("id", "1");
        context.put("name", "b");
        context.put("sex", "man");
        context.put("age", "999");
        System.out.println(context);
        // 单个字符的数字型字符串
        System.out.println(getValue("id == '1'", context));// false
        System.out.println(getValue("id == '1'.toString()", context));// true
        // 单个字符的非数字型字符串
        try {
            System.out.println(getValue("name == 'b'", context));
        } catch (Exception e) {
            // 'b'不能被转成数值型，此处会抛出NumberFormatException
            e.printStackTrace();
        }
        System.out.println(getValue("name == \"b\"", context));// true
        System.out.println(getValue("name == 'b'.toString()", context));// true

        // 不是单个字符的字符串
        System.out.println(getValue("sex == 'man'", context));// true
        System.out.println(getValue("sex == \"man\"", context));// true
        System.out.println(getValue("sex == 'man'.toString()", context));// true

        System.out.println(getValue("age == '999'", context));// true
        System.out.println(getValue("age == \"999\"", context));// true
        System.out.println(getValue("age == '999'.toString()", context));// true
    }

    public static Object getValue(String expression, Object map) throws OgnlException {
        return Ognl.getValue(expression, map);
    }


}
