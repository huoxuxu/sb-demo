package com.hxx.sbcommon.common.io.cfg;

import com.hxx.sbcommon.common.io.ReaderUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-08-30 16:24:36
 **/
public class ResourcesUtil {

    /**
     * 读取resources下的文件
     * 如：tmp/parkInfo.json
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readString(String fileName) throws IOException {
        InputStream in = ResourcesUtil.class.getResourceAsStream("/" + fileName);
        if (in == null) return null;

        try (InputStreamReader isr = new InputStreamReader(in)) {
            return ReaderUtil.readTxt(isr, 16 * 1024);
        }
    }
}
