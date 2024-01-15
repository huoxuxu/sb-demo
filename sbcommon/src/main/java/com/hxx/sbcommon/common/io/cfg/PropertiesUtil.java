package com.hxx.sbcommon.common.io.cfg;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-06-14 9:41:10
 **/
public class PropertiesUtil {
    /**
     * 获取配置
     *
     * @param cfgName
     * @param propKey
     * @return
     */
    public static String get(String cfgName, String propKey) throws IOException {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(cfgName)) {
            // 用Properties实例加载输入流pro。load(fis);
            prop.load(fis);
            return prop.getProperty(propKey);

        }
    }

}
