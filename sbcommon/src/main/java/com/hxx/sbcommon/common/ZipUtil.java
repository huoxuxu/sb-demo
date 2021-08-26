package com.hxx.sbcommon.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author liming
 * @description 字符串压缩和解压缩
 */
public class ZipUtil {

    //zip压缩
    public static String zipString(String str) {
        try {
            ByteArrayOutputStream bos = null;
            GZIPOutputStream os = null;
            byte[] bs = null;
            try {
                bos = new ByteArrayOutputStream();
                os = new GZIPOutputStream(bos);
                os.write(str.getBytes());
                os.close();
                bos.close();
                bs = bos.toByteArray();
                return new String(bs, "iso-8859-1");
            } finally {
                bs = null;
                bos = null;
                os = null;
            }
        } catch (Exception ex) {
            return str;
        }
    }

    //解压缩
    public static String unzipString(String str) {
        ByteArrayInputStream bis = null;
        ByteArrayOutputStream bos = null;
        GZIPInputStream is = null;
        byte[] buf = null;
        try {
            bis = new ByteArrayInputStream(str.getBytes("iso-8859-1"));
            bos = new ByteArrayOutputStream();
            is = new GZIPInputStream(bis);
            buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            is.close();
            bis.close();
            bos.close();
            return new String(bos.toByteArray());
        } catch (Exception ex) {
            return str;
        } finally {
            bis = null;
            bos = null;
            is = null;
            buf = null;
        }
    }
}
