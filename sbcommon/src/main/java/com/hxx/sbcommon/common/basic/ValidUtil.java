package com.hxx.sbcommon.common.basic;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 验证
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-08 15:16:45
 **/
@Slf4j
public class ValidUtil {
    // 字符串
    /**
     * 判断字符串是否全是字母
     * 支持大小写字母
     *
     * @param str
     * @return
     */
    public static boolean isAlpha(String str) {
        return StringUtils.isAlpha(str);
    }

    /**
     * 判断字符串是否全是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return StringUtils.isNumeric(str);
    }

}
