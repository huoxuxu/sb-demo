//package com.hxx.sbcommon.common.io.stream;
//
//import java.io.IOException;
//import java.io.Reader;
//
///**
// * 字符流解析
// *
// * @Author: huoxuxu
// * @Description:
// * @Date: 2024-01-17 10:13:33
// **/
//public class BaseCharStreamParser {
//
//    private final Reader reader;
//
//    // 当前流的读取位置
//    private int currentPosition = -1;
//    // 当前字符
//    private char current;
//
//    // 缓冲次数
//    private int currentCycle;
//    // 字符缓冲区
//    private char[] buf = new char[4 * 1024];
//    // 字符缓冲区长度
//    private int bufLength;
//    // 字符缓冲区开始时的位置
//    private int startPosition;
//
//    public BaseCharStreamParser(Reader reader) throws IOException {
//        this.reader = reader;
//        buf();
//    }
//
//    private void buf() throws IOException {
//        this.bufLength = reader.read(buf);
//        this.startPosition = this.currentPosition;
//        if (this.currentPosition > -1) {
//
//        }
//    }
//
//    /**
//     * 读取下一字符
//     */
//    public void next() {
////        this.currentPosition++;
////        this.current = charAt(this.currentPosition);
//    }
//
//    /**
//     * 取索引位的字符
//     *
//     * @param index
//     * @return
//     */
//    public char charAt(int index) throws IOException {
//        if (index < this.startPosition) {
//            throw new IllegalArgumentException("不支持向前索引");
//        }
//        // 最大索引位
//        int maxBufPosition = this.currentPosition + this.bufLength;
//        // 如果在当前缓冲区
//        if (index <= maxBufPosition) {
//            // 直接从buf里取
//            // 对应的buf的索引
//            int bufIndex = index - this.startPosition;
//            return this.buf[bufIndex];
//        }
//
//        // 不在当前缓冲区
//        if (index > maxBufPosition + 1) {
//            throw new IllegalArgumentException("不支持索引值跳跃");
//        }
//
//        // 重新缓存
//        buf();
//
//
//        return 0;
//    }
//
//}
