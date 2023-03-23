package com.hxx.sbcommon.common.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * 写入器
 * BufferedWriter   字符缓冲输出流
 * FileWriter   用来写入字符串到文件
 * OutputStreamWriter 写入字符，同时可以设置编码集。
 * CharArrayWriter
 * PipedWriter
 * FilterWriter
 * PrintWriter
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-23 9:35:39
 **/
public class WriterUtil {

    public static void writeString(File f, String str) throws IOException {
        try (Writer out = new FileWriter(f)) {
            out.write(str);
        }
    }
}
