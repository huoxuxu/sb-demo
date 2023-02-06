package com.hxx.sbcommon.common.io;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Consumer;

/**
 * 文件工具类
 * 注意：FileReader不允许您指定平台默认编码以外的编码。
 * Charset cs=Charset.forName("US-ASCII")
 * "utf-8" Charsets.toCharset(charsetName)
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-22 14:17:30
 **/
public class FileUtil {

    /**
     * 读取所有文本
     *
     * @param file    new File("D:/javaexp/first.txt")
     * @param charset "utf-8" Charsets.toCharset(charsetName)
     * @return
     * @throws IOException
     */
    public static String readAllTxt(File file, Charset charset) throws IOException {
        return FileUtils.readFileToString(file, charset);
    }

    /**
     * 读取所有行
     *
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    public static List<String> readLines(File file, Charset charset) throws IOException {
        return FileUtils.readLines(file, charset);
    }

    /**
     * 读取文件字节
     *
     * @param file
     * @return
     */
    public static byte[] readAllBytes(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

    /**
     * 获取文件流
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getFileStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    /**
     * 消费文件流Reader
     *
     * @param file
     * @param charset
     * @param consumerReader
     * @throws IOException
     */
    public static void readFileReader(File file, Charset charset, Consumer<Reader> consumerReader) throws IOException {
        try (InputStream fis = getFileStream(file)) {
            try (InputStreamReader isr = new InputStreamReader(fis, charset)) {
                try (BufferedReader reader = new BufferedReader(isr)) {
                    consumerReader.accept(reader);
                }
            }
        }
    }

    /**
     * 逐行读取文件
     * 推荐使用
     *
     * @param file
     * @param charset
     * @param rowAct
     * @throws IOException
     */
    public static void readFileByFileInputStream(File file, Charset charset, Consumer<String> rowAct) throws IOException {
        readFileReader(file, charset, reader -> {
            if (reader instanceof BufferedReader) {
                BufferedReader bfr = (BufferedReader) reader;
                while (true) {
                    try {
                        String line = bfr.readLine();
                        if (line == null) {
                            break;
                        }
                        rowAct.accept(line);
                    } catch (IOException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        });
    }

    /**
     * 写入文本，不存在会自动创建
     *
     * @param file
     * @param charset
     * @param content
     * @throws IOException
     */
    public static void writeAllTxt(File file, Charset charset, String content) throws IOException {
        FileUtils.writeStringToFile(file, content, charset, false);
    }


    /**
     * 追加文本，不存在会自动创建
     *
     * @param file
     * @param charset
     * @param appendContent
     * @throws IOException
     */
    public static void appendAllTxt(File file, Charset charset, String appendContent) throws IOException {
        FileUtils.writeStringToFile(file, appendContent, charset, true);
    }

}
