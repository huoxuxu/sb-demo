package com.hxx.sbcommon.common.basic.text;

import java.util.ArrayList;
import java.util.List;
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
        return regexMatch(str, regex);
    }

    /**
     * 判断输入是否符合正则
     *
     * @param input
     * @param regex
     * @return
     */
    public static boolean regexMatch(String input, String regex) {
        Matcher m = Pattern.compile(regex).matcher(input);
        return m.matches();
    }

    public static List<String> regexMatchGroups(String input, String regex) {
        Matcher m = Pattern.compile(regex).matcher(input);
        int groupCount = m.groupCount();

        List<String> ls = new ArrayList<>();
        while (m.find()) {
            String group = m.group(0);
            ls.add(group);
//            System.out.println("Found value: " + group);
        }
        return ls;
    }
}
