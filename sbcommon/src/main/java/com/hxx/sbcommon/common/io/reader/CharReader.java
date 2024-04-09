package com.hxx.sbcommon.common.io.reader;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by code4wt on 17/5/11.
 */
public class CharReader {

    private static final int BUFFER_SIZE = 4 * 1024;

    private final Reader reader;

    private final char[] buffer;
    // reader源的位置
    private int readerPos;

    // 当前buffer中读取的数据位置
    private int pos;

    // 当前buffer中包含数据的长度
    private int size;

    public CharReader(Reader reader) {
        this(reader, BUFFER_SIZE);
    }

    public CharReader(Reader reader, int bufSize) {
        this.reader = reader;
        buffer = new char[bufSize];
    }

    /**
     * 返回reader源的position
     *
     * @return
     */
    public int getPosition() {
        return readerPos;
    }

    /**
     * 返回下一位的字符，pos不加1
     *
     * @return
     */
    public char peek() {
        if (pos >= size) {
            return (char) -1;
        }

        return buffer[Math.max(0, pos)];
    }

    /**
     * 返回 下一位的字符，并将 pos + 1，最后返回字符
     * 读取到结尾时返回-1
     *
     * @return
     * @throws IOException
     */
    public char next() throws IOException {
        if (!hasMore()) {
            return (char) -1;
        }
        char ch = buffer[pos];
        pos++;
        readerPos++;
        return ch;
    }

    /**
     * 后退一个字符
     */
    public void back() {
        pos = Math.max(0, --pos);
        readerPos = Math.max(0, --readerPos);
    }

    /**
     * 是否包含更多字符
     *
     * @return
     * @throws IOException
     */
    public boolean hasMore() throws IOException {
        if (pos < size) {
            return true;
        }

        fillBuffer();
        return pos < size;
    }

    private void fillBuffer() throws IOException {
        int n = reader.read(buffer);
        if (n == -1) {
            return;
        }

        pos = 0;
        size = n;
    }
}
