package com.hxx.sbcommon.common.basic.text;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-10 16:58:08
 **/
public class StringUtil {
    public StringUtil() {
    }

    public static boolean equals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return StringUtils.equalsIgnoreCase(str1, str2);
    }

    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    public static String trim(String str) {
        return StringUtils.trim(str);
    }

    public static String upperCase(String str) {
        return StringUtils.upperCase(str);
    }

    public static String lowerCase(String str) {
        return StringUtils.lowerCase(str);
    }

    public static boolean isAlpha(String str) {
        return StringUtils.isAlpha(str);
    }

    public static boolean isNumeric(String str) {
        return StringUtils.isNumeric(str);
    }

    public static boolean startsWith(String str1, String prefix) {
        return StringUtils.startsWith(str1, prefix);
    }

    public static boolean endsWith(String str, String suffix) {
        return StringUtils.endsWith(str, suffix);
    }

    public static boolean contains(String str, String searchStr) {
        return StringUtils.contains(str, searchStr);
    }

    /**
     * 分割字符串
     * 注意：
     * StringUtils.split("1aa,b，c", "a,");
     * 结果：["1","b，c"]
     *
     * @param str
     * @param splitStr
     * @return
     */
    public static String[] split(String str, String splitStr) {
        return StringUtils.split(str, splitStr);
    }

    /**
     * 分割字符串
     * StringUtils.split("1aa,b，c", "a,");
     * 结果：["1a","b，c"]
     *
     * @param str
     * @param splitStr
     * @return
     */
    public static String[] splitByWholeSeparator(String str, String splitStr) {
        return StringUtils.splitByWholeSeparator(str, splitStr);
    }

    /**
     * 字符串分割，支持多分隔符
     * 原始字符串："1,2,3，4"
     * 分隔符：",","，"
     * 结果：1 2 3 4
     *
     * @param str
     * @param splitStrs
     * @return
     */
    public static List<String> splitByWholeSeparators(String str, String... splitStrs) {
        if (StringUtils.isBlank(str)) {
            return new ArrayList<>();
        }
        if (splitStrs == null || splitStrs.length == 0) {
            return new ArrayList<>(Arrays.asList(str));
        }

        List<String> ls = new ArrayList<>();
        for (String splitStr : splitStrs) {
            if (ls.size() == 0) {
                String[] arr = StringUtils.splitByWholeSeparator(str, splitStr);
                for (String item : arr) {
                    if (!StringUtils.isBlank(item)) {
                        ls.add(item.trim());
                    }
                }
            } else {
                List<String> result = splits(ls, splitStr);
                ls = result;
            }
        }
        return ls;
    }

    /**
     * 输入字符串集合，集合元素都按 分隔符 分割后去除空或空格后返回,
     *
     * @param strs
     * @param splitStr
     * @return
     */
    public static List<String> splits(List<String> strs, String splitStr) {
        List<String> ls = new ArrayList<>();
        for (String str : strs) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            String[] arr = StringUtils.splitByWholeSeparator(str, splitStr);
            for (String item : arr) {
                if (!StringUtils.isBlank(item)) {
                    ls.add(item.trim());
                }
            }
        }
        return ls;
    }

    public static String replace(String text, String searchString, String replacement) {
        return StringUtils.replace(text, searchString, replacement);
    }

    public static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
        return elements == null ? null : String.join(delimiter, elements);
    }

    public static String toLowerCaseFirstOne(String s) {
        if (s != null && s.length() != 0) {
            return Character.isLowerCase(s.charAt(0)) ? s : Character.toLowerCase(s.charAt(0)) + s.substring(1);
        } else {
            return s;
        }
    }

    public static String toUpperCaseFirstOne(String s) {
        if (s != null && s.length() != 0) {
            return Character.isUpperCase(s.charAt(0)) ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
        } else {
            return s;
        }
    }

    /**
     * 左侧填充 eg.001
     *
     * @param src
     * @param len
     * @param ch  待填充值
     * @return
     */
    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charArr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charArr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charArr[i] = ch;
        }
        return new String(charArr);
    }

    /**
     * 左侧填充 eg.100
     *
     * @param src
     * @param len
     * @param ch  待填充值
     * @return
     */
    public static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charArr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charArr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charArr[i] = ch;
        }
        return new String(charArr);
    }

}

