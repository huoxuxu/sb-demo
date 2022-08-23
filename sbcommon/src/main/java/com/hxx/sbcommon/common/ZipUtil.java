package com.hxx.sbcommon.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author
 * @description 字符串压缩和解压缩
 */
public class ZipUtil {

    /**
     * zip压缩
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String zipString(String str) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (GZIPOutputStream os = new GZIPOutputStream(bos)) {
                os.write(str.getBytes());
                os.close();
                bos.close();
                byte[] bs = bos.toByteArray();
                return new String(bs, "iso-8859-1");
            }
        }
    }

    /**
     * 解压缩
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String unzipString(String str) throws IOException {
        byte[] bytes = str.getBytes("iso-8859-1");
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                try (GZIPInputStream is = new GZIPInputStream(bis)) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) != -1) {
                        bos.write(buf, 0, len);
                    }

                    return new String(bos.toByteArray());
                }
            }
        }
    }

}
