package com.hxx.sbrest.common.config;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-30 10:18:44
 **/
public class MyFilterOneConfig {
    private static String[] words;//存放规定的敏感字

    static{
        words = new String[]{"糟糕", "混蛋"};//定义敏感字符
    }

    /**
     * 创建过滤方法filter
     * 敏感字不为空的时候,分别对每一个敏感字循环一次,如果在param中发现敏感字则将其替换为“****”
     *
     * @param param
     * @return
     */
    public static String filter(String param) {
        try {
            if (words != null && words.length > 0) {
                for (int i = 0; i < words.length; i++) {
                    if (param.indexOf(words[i]) != -1) {
                        param = param.replaceAll(words[i], "****");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }

}
