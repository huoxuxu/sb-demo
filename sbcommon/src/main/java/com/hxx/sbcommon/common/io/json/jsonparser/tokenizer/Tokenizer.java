package com.hxx.sbcommon.common.io.json.jsonparser.tokenizer;

import com.hxx.sbcommon.common.io.json.jsonparser.common.util.CharReaderUtil;
import com.hxx.sbcommon.common.io.json.jsonparser.common.exception.JsonParseException;

import java.io.IOException;

/**
 * JSON分词器
 * Created by code4wt on 17/5/10.
 */
public class Tokenizer {

    private CharReaderUtil charReader;

    private TokenList tokens;

    /**
     * 分词
     *
     * @param charReader
     * @return
     * @throws IOException
     */
    public TokenList tokenize(CharReaderUtil charReader) throws IOException {
        this.charReader = charReader;
        this.tokens = new TokenList();
        // 开始分词
        tokenize();

        return tokens;
    }

    private void tokenize() throws IOException {
        // 使用do-while处理空文件
        Token token;
        do {
            token = start();
            tokens.add(token);
        } while (token.getTokenType() != TokenType.END_DOCUMENT);
    }

    private Token start() throws IOException {
        // 跳过空白字符
        char ch = skipWhiteSpace();
        if (ch == (char) -1) {
            return new Token(TokenType.END_DOCUMENT, null);
        }

        switch (ch) {
            case '{':
                return new Token(TokenType.BEGIN_OBJECT, String.valueOf(ch));
            case '}':
                return new Token(TokenType.END_OBJECT, String.valueOf(ch));
            case '[':
                return new Token(TokenType.BEGIN_ARRAY, String.valueOf(ch));
            case ']':
                return new Token(TokenType.END_ARRAY, String.valueOf(ch));
            case ',':
                return new Token(TokenType.SEP_COMMA, String.valueOf(ch));
            case ':':
                return new Token(TokenType.SEP_COLON, String.valueOf(ch));
            case 'n':
                return readNull();
            case 't':
            case 'f':
                return readBoolean(ch);
            case '"':
                return readString();
            case '-':
                return readNumber(ch);
        }

        // 读取数字
        if (isDigit(ch)) {
            return readNumber(ch);
        }

        throw new JsonParseException("Illegal character");
    }

    // 跳过空白字符，到结尾时返回-1
    private char skipWhiteSpace() throws IOException {
        char ch;
        do {
            ch = charReader.next();
        } while (ch == (char) -1 || isWhiteSpace(ch));
        return ch;
    }

    private boolean isWhiteSpace(char ch) {
        return (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n');
    }

    private Token readString() throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char ch = charReader.next();
            // 转义
            if (ch == '\\') {
                if (!isEscape()) {
                    throw new JsonParseException("Invalid escape character");
                }
                sb.append('\\');
                ch = charReader.peek();
                sb.append(ch);
                if (ch == 'u') {
                    for (int i = 0; i < 4; i++) {
                        ch = charReader.next();
                        if (isHex(ch)) {
                            sb.append(ch);
                        } else {
                            throw new JsonParseException("Invalid character");
                        }
                    }
                }
            }
            // 读到了双引号
            else if (ch == '"') {
                return new Token(TokenType.STRING, sb.toString());
            }
            // 读到了\r\n
            else if (ch == '\r' || ch == '\n') {
                throw new JsonParseException("Invalid character");
            }
            // 读到了其他字符
            else {
                sb.append(ch);
            }
        }
    }

    // 是否json支持的转义字符
    private boolean isEscape() throws IOException {
        char ch = charReader.next();
        return (ch == '"' || ch == '\\' || ch == 'u' || ch == 'r'
                || ch == 'n' || ch == 'b' || ch == 't' || ch == 'f');

    }

    private boolean isHex(char ch) {
        return ((ch >= '0' && ch <= '9') || ('a' <= ch && ch <= 'f')
                || ('A' <= ch && ch <= 'F'));
    }

    private Token readNumber(char ch) throws IOException {
        StringBuilder sb = new StringBuilder();
        // 处理负数
        if (ch == '-') {
            sb.append(ch);
            ch = charReader.next();
            if (ch == '0') {    // 处理 -0.xxxx
                sb.append(ch);
                sb.append(readFractionAndExp());
            } else if (isDigitOne2Nine(ch)) {
                do {
                    sb.append(ch);
                    ch = charReader.next();
                } while (isDigit(ch));
                if (ch != (char) -1) {
                    charReader.back();
                    sb.append(readFractionAndExp());
                }
            } else {
                throw new JsonParseException("Invalid minus number");
            }
        }
        // 处理小数
        else if (ch == '0') {
            sb.append(ch);
            sb.append(readFractionAndExp());
        }
        // 其他
        else {
            do {
                sb.append(ch);
                ch = charReader.next();
            } while (isDigit(ch));
            if (ch != (char) -1) {
                charReader.back();
                sb.append(readFractionAndExp());
            }
        }

        return new Token(TokenType.NUMBER, sb.toString());
    }

    private boolean isExp(char ch) {
        return ch == 'e' || ch == 'E';
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isDigitOne2Nine(char ch) {
        return ch >= '0' && ch <= '9';
    }

    // 处理小数
    private String readFractionAndExp() throws IOException {
        StringBuilder sb = new StringBuilder();
        char ch = charReader.next();
        if (ch == '.') {
            sb.append(ch);
            ch = charReader.next();
            if (!isDigit(ch)) {
                throw new JsonParseException("Invalid frac");
            }
            do {
                sb.append(ch);
                ch = charReader.next();
            } while (isDigit(ch));

            if (isExp(ch)) {    // 处理科学计数法
                sb.append(ch);
                sb.append(readExp());
            } else {
                if (ch != (char) -1) {
                    charReader.back();
                }
            }
        } else if (isExp(ch)) {
            sb.append(ch);
            sb.append(readExp());
        } else {
            charReader.back();
        }

        return sb.toString();
    }

    private String readExp() throws IOException {
        StringBuilder sb = new StringBuilder();
        char ch = charReader.next();
        if (ch == '+' || ch == '-') {
            sb.append(ch);
            ch = charReader.next();
            if (isDigit(ch)) {
                do {
                    sb.append(ch);
                    ch = charReader.next();
                } while (isDigit(ch));

                if (ch != (char) -1) {    // 读取结束，不用回退
                    charReader.back();
                }
            } else {
                throw new JsonParseException("e or E");
            }
        } else {
            throw new JsonParseException("e or E");
        }

        return sb.toString();
    }

    private Token readBoolean(char ch) throws IOException {
        if (ch == 't') {
            if (!(charReader.next() == 'r' && charReader.next() == 'u' && charReader.next() == 'e')) {
                throw new JsonParseException("Invalid json string");
            }

            return new Token(TokenType.BOOLEAN, "true");
        } else {
            if (!(charReader.next() == 'a' && charReader.next() == 'l'
                    && charReader.next() == 's' && charReader.next() == 'e')) {
                throw new JsonParseException("Invalid json string");
            }

            return new Token(TokenType.BOOLEAN, "false");
        }
    }

    private Token readNull() throws IOException {
        if (!(charReader.next() == 'u' && charReader.next() == 'l' && charReader.next() == 'l')) {
            throw new JsonParseException("Invalid json string");
        }

        return new Token(TokenType.NULL, "null");
    }
}
