package com.hxx.sbcommon.common.basic.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-07-20 13:18:14
 **/
public class RegexUtil {

    /**
     * 判断字符串中是否包含字母
     * 使用正则表达式
     *
     * @param str 待检验的字符串
     * @return 返回是否包含 true: 包含字母 ;false 不包含字母
     */
    public static boolean isHasAlphabet(String str) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(str);
        return m.matches();
    }


}
