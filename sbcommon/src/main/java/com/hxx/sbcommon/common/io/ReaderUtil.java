package com.hxx.sbcommon.common.io;

import org.apache.http.util.CharArrayBuffer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-05 11:52:26
 **/
public class ReaderUtil {

    /**
     * 读取字符流
     *
     * @param reader
     * @param bufSize
     * @return
     * @throws IOException
     */
    public static String readTxt(Reader reader, int length, int bufSize) throws IOException {
        CharArrayBuffer buffer = new CharArrayBuffer(length);
        char[] tmp = new char[bufSize];

        int l;
        while ((l = reader.read(tmp)) != -1) {
            buffer.append(tmp, 0, l);
        }

        return buffer.toString();
    }

    /**
     * 读取字符流
     *
     * @param reader
     * @param bufSize
     * @return
     * @throws IOException
     */
    public static String readTxt(Reader reader, int bufSize) throws IOException {
        List<Character> ls = new ArrayList<>();
        char[] tmp = new char[bufSize];

        int l;
        while ((l = reader.read(tmp)) != -1) {
            for (int i = 0; i < l; i++) {
                ls.add(tmp[i]);
            }
        }
        char[] charArr = new char[ls.size()];
        for (int i = 0; i < ls.size(); i++) {
            charArr[i] = ls.get(i);
        }
        return new String(charArr, 0, charArr.length);
    }

}
