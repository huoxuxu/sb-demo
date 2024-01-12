package com.hxx.sbcommon.common.io.fileOrDir;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Consumer;

/**
 * 文件工具类
 * 注意：FileReader不允许您指定平台默认编码以外的编码。
 * Charset.forName("US-ASCII")
 * Charsets.toCharset("utf-8")
 * StandardCharsets.UTF_8
 * StandardCharsets.US_ASCII
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-04 13:16:38
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
    public static String readAllText(File file, Charset charset) throws IOException {
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
     * 读取文件字节
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] readByteArr(File file) throws IOException {
        int size = (int) file.length();
        byte[] data = new byte[size];

        try (InputStream is = Files.newInputStream(file.toPath())) {
            int offset = 0;
            while (offset < size) {
                int ret = is.read(data, offset, size - offset);
                if (ret == -1) {
                    break;
                }

                offset += ret;
            }
        }

        return data;
    }

    /**
     * 获取文件流
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getFileStream(File file) throws IOException {
        return Files.newInputStream(file.toPath());
    }

    /**
     * 消费文件流Reader
     *
     * @param file
     * @param charset
     * @param consumerReader
     * @throws IOException
     */
    public static void readFileBufferedReader(File file, Charset charset, Consumer<BufferedReader> consumerReader) throws IOException {
        try (InputStream fis = getFileStream(file)) {
            try (InputStreamReader isr = new InputStreamReader(fis, charset)) {
                try (BufferedReader reader = new BufferedReader(isr)) {
                    consumerReader.accept(reader);
                }
            }
        }
    }

    /**
     * 逐行读取文件,包含空行
     * 推荐使用
     *
     * @param file
     * @param charset
     * @param rowAct
     * @throws IOException
     */
    public static void readLines(File file, Charset charset, Consumer<String> rowAct) throws IOException {
        readFileBufferedReader(file, charset, reader -> {
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    rowAct.accept(line);
                } catch (IOException ex) {
                    throw new IllegalStateException(ex);
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
