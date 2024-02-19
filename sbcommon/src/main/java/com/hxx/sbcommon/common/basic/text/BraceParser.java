package com.hxx.sbcommon.common.basic.text;

import com.hxx.sbcommon.common.io.reader.CharReader;

import java.io.*;

/**
 * 解析字符串中的{}占位符
 * 转义：{{=>{, }}=>}
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-01-17 9:30:31
 **/
public class BraceParser implements AutoCloseable {
    //严谨性，默认true
    public boolean strict = true;

    private final Reader reader;
    private final CharReader charReader;

    public BraceParser(Reader reader) {
        this.reader = reader;
        this.charReader = new CharReader(this.reader);
    }

    public BraceParser(String input) {
        this(new StringReader(input));
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            reader.close();
        }
    }

    /**
     * 解析字符串
     *
     * @param input 1{}2{}3
     * @param args  a,b
     * @return 1a2b3
     * @throws Exception
     */
    public static String parse(String input, Object... args) {
        try (BraceParser parser = new BraceParser(input)) {
            parser.setStrict(false);
            return parser.parse(args);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String parseByStrict(String input, Object... args) {
        try (BraceParser parser = new BraceParser(input)) {
            parser.setStrict(true);
            return parser.parse(args);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 解析字符串
     *
     * @param args
     * @return
     * @throws IOException
     */
    public String parse(Object... args) throws IOException {
        StringBuilder sb = new StringBuilder();
        int argInd = 0;
        while (true) {
            char ch = charReader.next();
            if (ch == (char) -1) {
                break;
            }

            if (ch == '{') {
                ch = charReader.peek();
                // 转义的
                if (ch == '{') {
                    ch = charReader.next();
                    sb.append(ch);
                }
                // 正常结束的
                else if (ch == '}') {
                    ch = charReader.next();
                    Object argVal = getArgVal(args, argInd);
                    sb.append(argVal);
                    argInd++;
                }
                // 错误的，{x}
                else {
                    if (strict) {
                        throw new IllegalArgumentException("格式错误，位置：" + charReader.getPosition());
                    }
                    sb.append('{');
                }
            } else if (ch == '}') {
                ch = charReader.peek();
                if (ch == '}') {
                    ch = charReader.next();
                    sb.append(ch);
                }
                // 错误的，{x}
                else {
                    if (strict) {
                        throw new IllegalArgumentException("格式错误，位置：" + charReader.getPosition());
                    }
                    sb.append('}');
                }
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * 设置严格、宽松模式
     *
     * @param strict true 严格，false 宽松
     */
    public void setStrict(boolean strict) {
        this.strict = strict;
    }


    // 获取提供的值
    private Object getArgVal(Object[] args, int ind) {
        if (args == null) {
            return "";
        } else if (ind < args.length) {
            return args[ind];
        } else {
            return "";
        }
    }

}
