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

    /**
     * 取字符串的前n位,
     * 超过字符串总长度不会报错
     *
     * @param str
     * @param count
     * @return
     */
    public static String cut(String str, int count) {
        if (StringUtils.isEmpty(str)) return str;
        if (count == 0) return "";

        int len = str.length();
        if (count >= len) return str;

        return str.substring(0, count);
    }

    /**
     * 截取到指定字符串截止，但不包含指定字符串
     *
     * @param src
     * @param subStr
     * @return
     */
    private static String substring(String src, String subStr) {
        int ind = src.indexOf(subStr);
        return ind == -1 ? src : src.substring(0, ind);
    }

    /**
     * 小写第一位
     */
    public static String lowerFirstChar(String str) {
        if (null == str) return null;

        if (str.length() > 0) {
            char firstChar = str.charAt(0);
            if (Character.isUpperCase(firstChar)) {
                return Character.toLowerCase(firstChar) + str.substring(1);
            }
        }
        return str;
    }

    /**
     * 大写第一位
     *
     * @param str
     * @return
     */
    public static String upperFirstChar(String str) {
        if (null == str) return null;

        if (str.length() > 0) {
            char firstChar = str.charAt(0);
            if (Character.isLowerCase(firstChar)) {
                return Character.toUpperCase(firstChar) + str.substring(1);
            }
        }
        return str;
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
     * 获取第一个分隔符的键和值
     *
     * @param str
     * @param splitStr
     * @return
     */
    public static String[] splitFirst(String str, String splitStr) {
        int i = str.indexOf(splitStr);
        if (i == -1) {
            String[] arr = {str, ""};
            return arr;
        } else {
            String v = "";
            if (str.length() > i + 1) {
                v = str.substring(i + 1);
            }
            String[] arr = {str.substring(0, i), v};
            return arr;
        }
    }

    /**
     * 修剪开始位以 trimStr 开头的字符
     *
     * @param str
     * @param trimStr
     * @return
     */
    public static String trimStart(String str, String trimStr) {
        if (StringUtils.isEmpty(trimStr)) return str;

        if (str.equals(trimStr)) return "";

        int ind = str.indexOf(trimStr);
        if (ind == 0) {
            str = str.substring(trimStr.length());
            return trimStart(str, trimStr);
        }

        return str;
    }

    /**
     * 获取字符串中最后的数字
     *
     * @param str
     * @return
     */
    public static String getEndNumber(String str) {
        char[] carr = new char[str.length()];
        int j = 0;
        // 逆序遍历
        for (int i = str.length() - 1; i > -1; i--) {
            char c = str.charAt(i);
            if (!Character.isDigit(c)) break;

            carr[carr.length - 1 - j] = c;
            j++;
        }
        if (j == 0) return "";

        // 这里没测试
        return String.copyValueOf(carr, carr.length - j, j);
//            List<Character> cls = new ArrayList<>();
//            for (int i = str.length() - 1; i > -1; i--) {
//                char c = str.charAt(i);
//                if (!Character.isDigit(c)) {
//                    break;
//                }
//                cls.add(c);
//            }
//            if (CollectionUtils.isEmpty(cls)) return "";
//
//            // 反转
//            Collections.reverse(cls);
//
//            StringBuilder sb = new StringBuilder();
//            for (Character cl : cls) {
//                sb.append(cl);
//            }
//
//            return sb.toString();
    }


    /**
     * 分割字符串，支持多分隔符
     * 原始字符串："1,2,3，4"
     * 分隔符：[",","，"]
     * 结果：1 2 3 4
     *
     * @param str
     * @param splitStrs
     * @return
     */
    public static List<String> splitByWholeSeparators(String str, String... splitStrs) {
        if (StringUtils.isBlank(str)) return new ArrayList<>();

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

    /**
     * 左侧填充 eg.001
     *
     * @param src
     * @param len 填充后长度
     * @param ch  待填充值
     * @return
     */
    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff < 1) {
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
     * @param len 填充后长度
     * @param ch  待填充值
     * @return
     */
    public static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff < 1) {
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

