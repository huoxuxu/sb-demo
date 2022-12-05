package com.hxx.sbcommon.common.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-02 10:35:09
 **/
public class CodecUtil {
    private CodecUtil() {
    }

    public static String encodeForUTF8(String str) {
        return encode(str, "UTF-8");
    }

    public static String encode(String str, String charset) {
        try {
            String target = URLEncoder.encode(str, charset);
            return target;
        } catch (UnsupportedEncodingException var4) {
            throw new IllegalArgumentException("CodecUtil.encode编码异常" + str, var4);
        }
    }

    public static String decodeForUTF8(String str) {
        return decode(str, "UTF-8");
    }

    public static String decode(String str, String charset) {
        try {
            String target = URLDecoder.decode(str, charset);
            return target;
        } catch (UnsupportedEncodingException var4) {
            throw new IllegalArgumentException("CodecUtil.decode解码异常" + str, var4);
        }
    }

    public static String encodeForBase64(String str) {
        return Base64.encodeBase64String(str.getBytes());
    }

    public static String encodeForBase64(byte[] str) {
        return Base64.encodeBase64String(str);
    }

    public static String decodeForBase64(String str) {
        return new String(Base64.decodeBase64(str.getBytes()));
    }

    public static String encryptForMD5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String encryptForSHA(String str) {
        return DigestUtils.sha512Hex(str);
    }

    public static String createRandomNumber(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    public static String createUUID() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "");
    }

    public static String encodeFile(String filePath) throws IOException {
        byte[] bytes = fileToByte(filePath);
        return encodeForBase64(bytes);
    }

    public static void decodeToFile(String filePath, String base64) throws IOException {
        byte[] bytes = decodeForBase64(base64).getBytes();
        byteArrayToFile(bytes, filePath);
    }

    public static byte[] fileToByte(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    public static void byteArrayToFile(byte[] bytes, String filePath) throws IOException {
        InputStream in = new ByteArrayInputStream(bytes);
        File destFile = new File(filePath);
        if (!destFile.getParentFile()
                .exists()) {
            destFile.getParentFile()
                    .mkdirs();
        }

        if (destFile.createNewFile()) {
            Files.copy(in, Paths.get(filePath), new CopyOption[]{StandardCopyOption.ATOMIC_MOVE});
        } else {
            throw new IOException("CREATE FILE ERROR FILENAME" + destFile.getName());
        }
    }
}
