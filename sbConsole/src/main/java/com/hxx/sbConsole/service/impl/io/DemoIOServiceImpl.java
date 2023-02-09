package com.hxx.sbConsole.service.impl.io;

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
        String path = FileUtil.combine("d:", "998.txt");
        File file = new File(path);
        FileUtil.writeAllTxt(file, StandardCharsets.UTF_8, "aabbcc");

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
