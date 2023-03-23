package com.hxx.sbConsole.service.impl.demo.io;

import com.hxx.sbcommon.common.io.FileUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-02-06 9:32:03
 **/
public class DemoIOServiceImpl {
    public static void demo() throws Exception {
        System.out.println("FileUtil==================================================");
        {
            String path = FileUtil.combine("d:", "998.txt");
            File file = new File(path);

            String content = "aabbcc";
            // 写入文件
            FileUtil.writeAllTxt(file, StandardCharsets.UTF_8, content);

            System.out.println("file.length()= " + file.length());
            byte[] byteArr = FileUtil.readAllBytes(file);
            System.out.println("byteArr.length()= " + byteArr.length);
            byte[] byteArr2 = FileUtil.readByteArr(file);
            System.out.println("byteArr2.length()= " + byteArr2.length);
        }
    }

    public static void main(String[] args) {
        try {
            demo();
            System.out.println("ok!");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }
}
