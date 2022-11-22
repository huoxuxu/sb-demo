package com.hxx.sbcommon.common.io;

import java.io.*;
import java.nio.charset.Charset;
import java.util.function.Consumer;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-22 14:17:30
 **/
public class FileUtil {

    /**
     * 逐行读取文件
     * 推荐使用
     *
     * @param file
     * @param charset 可以为null，demo Charset cs=Charset.forName("US-ASCII")
     * @param rowAct
     * @throws IOException
     */
    public static void readFileByFileInputStream(File file, Charset charset, Consumer<String> rowAct) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bfr = new BufferedReader(isr);
            while (true) {
                String line = bfr.readLine();
                if (line == null) {
                    break;
                }
                rowAct.accept(line);
            }
        }
    }

    /**
     * 逐行读取文件,
     * 注意：FileReader不允许您指定平台默认编码以外的编码。
     *
     * @param file
     * @param rowAct
     * @throws IOException
     */
    public static void readFileByFileReader(File file, Consumer<String> rowAct) throws IOException {
        try (FileReader fr = new FileReader(file)) {
            BufferedReader bfr = new BufferedReader(fr);
            while (true) {
                String line = bfr.readLine();
                if (line == null) {
                    break;
                }
                rowAct.accept(line);
            }
        }
    }

}
