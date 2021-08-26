package com.hxx.sbweb.common;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-28 18:17:58
 **/
public class StringUtils2 {
    /**
     * 确保开头字符为给定内容
     *
     * @param checkStr    原始字符串
     * @param headContent 头部内容
     * @return {@link String}
     */
    public static String ensureStartWith(String checkStr, String headContent) {
        if (!checkStr.startsWith(headContent)) {
            checkStr = headContent + checkStr;
        }
        return checkStr;
    }


}
