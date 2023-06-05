package com.hxx.sbConsole.module.textParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-06-03 17:51:00
 **/
public class StringEscapDemoService {
    public static void main(String[] args) {
        try {
            demo();
        } catch (Exception ex) {
            System.out.println(ex + "");
        }
    }


    // 字符串使用双引号转义
    static void demo() throws IOException {
        // 假设字符串是这样的："Hello ""World"""
        String str = "Hello \"\"World\"\"";

// 创建一个字符流，将字符串作为输入源
        Reader reader = new StringReader(str);

// 创建一个bufferReader，包装字符流
        BufferedReader bufferedReader = new BufferedReader(reader);

// 创建一个结果字符串
        StringBuilder result = new StringBuilder();

// 读取字符流中的每一个字符
        int ch;
        while ((ch = bufferedReader.read()) != -1) {
            // 如果字符是双引号，判断下一个字符是否也是双引号
            if (ch == '"') {
                // 读取下一个字符
                int next = bufferedReader.read();
                // 如果下一个字符也是双引号，说明是转义的双引号，添加一个双引号到结果字符串
                if (next == '"') {
                    result.append('"');
                } else {
                    // 否则，将下一个字符添加到结果字符串
                    result.append((char) next);
                }
            } else {
                // 否则，将字符添加到结果字符串
                result.append((char) ch);
            }
        }

// 关闭bufferReader和字符流
        bufferedReader.close();
        reader.close();

// 打印结果字符串
        System.out.println(result.toString()); // Hello "World"


    }

}
