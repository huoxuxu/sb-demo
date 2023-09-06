package com.hxx.sbcommon.common.io.cfg;

import com.hxx.sbcommon.common.io.ReaderUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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

    public void loadResFile() throws IOException {
        //获取文件的URL
        File file = ResourceUtils.getFile("classpath:json/person.json");
        System.out.println("文件:" + file.getPath());
        //转成string输入文本
        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        System.out.println("内容：" + content);
    }
}
