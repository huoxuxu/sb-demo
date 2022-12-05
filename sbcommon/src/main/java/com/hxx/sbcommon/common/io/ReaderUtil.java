package com.hxx.sbcommon.common.io;

import org.apache.http.util.CharArrayBuffer;

import java.io.IOException;
import java.io.Reader;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-05 11:52:26
 **/
public class ReaderUtil {

    /**
     * @param reader
     * @param bufSize
     * @return
     * @throws IOException
     */
    public static String readTxt(Reader reader, int bufSize) throws IOException {
        CharArrayBuffer buffer = new CharArrayBuffer(bufSize);
        char[] tmp = new char[1024];

        int l;
        while ((l = reader.read(tmp)) != -1) {
            buffer.append(tmp, 0, l);
        }

        return buffer.toString();
    }
}
