package com.hxx.sbcommon.common.io;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 流操作
 * InputStream
 * ByteArrayOutputStream 内存流
 * FileInputStream 文件流
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-23 10:40:56
 **/
public class StreamUtil {

    /**
     * 读取流中数据到字节数组
     *
     * @param
     * @param totalData 字节数组
     * @param totalSize 字节数组的总长度
     * @return
     * @throws IOException
     */
    public static void readByteArr(InputStream inStream, byte[] totalData, int totalSize) throws IOException {
        int offset = 0;
        while (offset < totalSize) {
            int ret = inStream.read(totalData, offset, totalSize - offset);
            if (ret == -1) {
                break;
            }

            offset += ret;
        }
    }


    /**
     * 读取流到字节数组，不关闭输入流
     *
     * @param inStream
     * @param bufSize
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inStream, int bufSize) throws IOException {
        return readInputStream(inStream, bufSize, false);
    }

    /**
     * 读取流到字节数组
     *
     * @param inStream
     * @param closeInStream 是否关闭流
     * @param bufSize
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream, int bufSize, boolean closeInStream) throws IOException {
        byte[] buffer = new byte[bufSize];
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = inStream.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }

            if (closeInStream) {
                //关闭输入流
                inStream.close();
            }

            //把outStream里的数据写入内存
            return outStream.toByteArray();
        }
    }

}
