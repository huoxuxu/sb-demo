package com.hxx.sbConsole.service.impl.json;

import com.hxx.sbcommon.common.io.json.JacksonReaderQuick;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Scanner;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-09 13:56:49
 **/
@Slf4j
@Service
public class JacksonServiceImpl {

    public static void main(String[] args) {
        JacksonServiceImpl.demo();
        System.out.println("ok1");
        Scanner input = new Scanner(System.in);
        input.next();
        System.out.println("ok");
    }

    public static void demo() {
        try {
            String path = "d:/demo1.json";
            long start = System.currentTimeMillis();
            parse1(path);
            System.out.println("耗时：" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            System.out.println(e + "");
        }
    }

    private static void parse1(String path) throws IOException {
        int total = JacksonReaderQuick.parse(path,om->{
            System.out.println(om);
        });
        System.out.println("共：" + total);
    }

}
