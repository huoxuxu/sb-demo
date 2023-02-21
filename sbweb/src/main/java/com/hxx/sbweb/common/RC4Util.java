package com.hxx.sbweb.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * RC4加密
 *
 * @author xiyunfei
 * @date 2021/01/22
 */
public class RC4Util {

    /**
     * RC4加密，将加密后的数据进行哈希
     *
     * @param data     需要加密的数据
     * @param key      加密密钥
     * @param chartSet 编码格式，默认UTF-8
     * @return 返回加密后的数据
     */
    public static String encryRC4Hex(String data, String key, Charset chartSet) {
        if (data == null || key == null) {
            return null;
        }
        if (chartSet == null) chartSet = StandardCharsets.UTF_8;

        return DigestUtil.bytesToHex(encryRC4Byte(data, key, chartSet));
    }

    /**
     * RC4加密,将加密后的数据转为字符串
     *
     * @param data     需要加密的数据
     * @param key      加密密钥
     * @param chartSet 编码格式，默认UTF-8
     * @return
     */
    public static String encryRC4Str(String data, String key, Charset chartSet) {
        if (data == null || key == null) {
            return null;
        }
        if (chartSet == null) {
            chartSet = StandardCharsets.UTF_8;
        }
        return new String(encryRC4Byte(data, key, chartSet));
    }

    /**
     * RC4加密，将加密后的字节数据
     *
     * @param data     需要加密的数据
     * @param key      加密密钥
     * @param chartSet 编码方式 默认UTF-8
     * @return 返回加密后的数据
     */
    public static byte[] encryRC4Byte(String data, String key, Charset chartSet) {
        if (data == null || key == null) {
            return null;
        }
        if (chartSet == null) {
            chartSet = StandardCharsets.UTF_8;
        }
        byte[] bData = data.getBytes(chartSet);
        return RC4Base(bData, key);
    }

    /**
     * RC4解密
     *
     * @param data     需要解密的数据
     * @param key      加密密钥
     * @param chartSet 编码方式 默认UTF-8
     * @return 返回解密后的数据
     */
    public static String decryRC4(String data, String key, Charset chartSet) {
        if (data == null || key == null) {
            return null;
        }
        if (chartSet == null) {
            chartSet = StandardCharsets.UTF_8;
        }
        return new String(RC4Base(DigestUtil.hexToByte(data), key), chartSet);
    }

    /**
     * RC4加密初始化密钥
     *
     * @param aKey
     * @return 密钥
     */
    private static byte[] initKey(String aKey) {
        byte[] bKey = aKey.getBytes();
        if (aKey == null || bKey.length == 0) {
            return null;
        }
        byte[] state = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (bKey.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((bKey[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % bKey.length;
        }
        return state;
    }

    /**
     * RC4解密
     *
     * @param input 输入内容
     * @param mkKey 加密令牌
     * @return 密钥
     */
    private static byte[] RC4Base(byte[] input, String mkKey) {
        int x = 0;
        int y = 0;
        byte[] key = initKey(mkKey);
        if (key == null) {
            return null;
        }
        int xorIndex;
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }
}
