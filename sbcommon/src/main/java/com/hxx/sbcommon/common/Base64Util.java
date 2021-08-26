package com.hxx.sbcommon.common;

import java.util.Base64;

public class Base64Util {
    static final Base64.Decoder decoder = Base64.getDecoder();
    static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * 编码
     *
     * @param txt
     * @return
     * @throws Exception
     */
    public static String encode(String txt) throws Exception {
        byte[] textByte = txt.getBytes("UTF-8");
        return encoder.encodeToString(textByte);
    }

    /**
     * 解码
     *
     * @param txt
     * @return
     * @throws Exception
     */
    public static String decode(String txt) throws Exception {
        byte[] arr = decoder.decode(txt);
        return new String(arr, "UTF-8");
    }

}
