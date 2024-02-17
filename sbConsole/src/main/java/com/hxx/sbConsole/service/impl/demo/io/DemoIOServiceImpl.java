package com.hxx.sbConsole.service.impl.demo.io;

import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.io.cfg.ResourcesUtil;
import com.hxx.sbcommon.common.io.fileOrDir.FileUtil;
import com.hxx.sbcommon.common.io.fileOrDir.PathUtil;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-02-06 9:32:03
 **/
public class DemoIOServiceImpl {
    public static void main(String[] args) {
        try {
            {
                {
                    String txt = "1我2爱3北4京5天6安7们890";
                    char[] buf = new char[2];
                    StringReader sr = new StringReader(txt);
                    for (int i = 0; i < 3; i++) {
                        int read = sr.read(buf);

                        char[] buf2 = new char[read];
                        System.arraycopy(buf, 0, buf2, 0, read);
                        System.out.println("read: " + JsonUtil.toJSON(buf2));
                    }

                    System.out.println("ok");
                }
                {
                    String txt = "\\123\"00\\{{k12}}45\t6";

                    String path = "d:/tmp/12.txt";
                    FileUtil.writeAllTxt(new File(path), StandardCharsets.UTF_8, txt);

                    char[] chars = txt.toCharArray();
                    System.out.println(JsonUtil.toJSON(chars));
                    System.out.println("=============================");

                    String path1 = "demo/1.txt";
//                    path="classpath:";
                    String str1 = ResourcesUtil.readString(path1);
                    System.out.println(str1);
                    System.out.println("length: " + str1.length());

                    byte[] bytes = str1.getBytes(StandardCharsets.UTF_8);
                    System.out.println(StringUtils.join(bytes, ','));
                }
            }
            PathUtil_Demo();
            case1();
            demo();
            System.out.println("ok!");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    static void PathUtil_Demo() {
        String pathVal = "d:/998.txt";
        String path = "";
        path = PathUtil.combine("d:", "998.txt");
        OftenUtil.assertCond(!path.equals(pathVal), "验证失败！");
        path = PathUtil.combine("d:/", "998.txt");
        OftenUtil.assertCond(!path.equals(pathVal), "验证失败！");
        path = PathUtil.combine("d:", "/998.txt");
        OftenUtil.assertCond(!path.equals(pathVal), "验证失败！");
        path = PathUtil.combine("d:/", "/998.txt");
        OftenUtil.assertCond(!path.equals(pathVal), "验证失败！");
        path = PathUtil.combine("d://", "//998.txt");
        OftenUtil.assertCond(!path.equals(pathVal), "验证失败！");

        System.out.println(path);
    }

    public static void demo() throws Exception {
        System.out.println("FileUtil==================================================");
        {
            String path = PathUtil.combine("d:", "998.txt");
            File file = new File(path);

            String content = "aabbcc";
            // 写入文件
            FileUtil.writeAllTxt(file, StandardCharsets.UTF_8, content);

            System.out.println("file.length()= " + file.length());
            byte[] byteArr = FileUtil.readAllBytes(file);
            System.out.println("byteArr.length()= " + byteArr.length);
            byte[] byteArr2 = FileUtil.readByteArr(file);
            System.out.println("byteArr2.length()= " + byteArr2.length);
        }
    }

    public static void case1() throws IOException {
        String path = "d:/tmp/tmp1.txt";
        File file = new File(path);
        List<String> ls = new ArrayList<>();
        FileUtil.readLines(file, StandardCharsets.UTF_8, line -> {
            if (StringUtils.isBlank(line)) return;
            line = line.trim();
            String[] arr = line.split(":");
            if (arr.length != 2) {
                System.out.println("err：" + line);
                return;
            }
/*
 { "JavaType": "String", "JdbcType": "VARCHAR,LONGVARCHAR" },
 */
            String msg = "{ \"JdbcType\": \"" + arr[0] + "\", \"JavaType\": \"" + arr[1] + "\" }";
            ls.add(msg);
        });

        System.out.println("[" + StringUtils.join(ls, ",") + "]");
    }

    public static void case2() {

    }

}
