//package com.hxx.sbcommon.common.basic.text;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.StringReader;
//
///**
// * 解析字符串中的{}占位符
// * 转义：{{=>{, }}=>}
// *
// * @Author: huoxuxu
// * @Description:
// * @Date: 2024-01-17 9:30:31
// **/
//public class BraceParser {
//
//    private final InputStreamReader reader;
//    private String charsetName;
//
//    public BraceParser(InputStreamReader reader, String charsetName) {
//        this.reader = reader;
//        this.charsetName = charsetName;
//    }
//
//    public BraceParser(InputStreamReader reader) {
//        this(reader, null);
//    }
//
//    public BraceParser(String input) {
//        this.reader = new BufferedReader(input);
//    }
//
//}
