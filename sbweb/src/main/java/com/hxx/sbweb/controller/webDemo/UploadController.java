package com.hxx.sbweb.controller.webDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {
    /**
     * 请求参数名和方法参数名不一致
     * http://localhost:8082/reqpara/getname?name=jay
     *
     * @param str
     * @return
     */
    @GetMapping("/uploadfile")
    public String getname(@RequestParam(value = "name") String str) {
        return "name is:" + str;
    }

    public static void main(String[] args) {
        {
            String t1 = new String("11**&&##@!@#$&&@*");
            t1.intern();
            String t2 = "11**&&##@!@#$&&@*";
            System.out.println(t1 == t2);
        }

        {
            String t3 = new String("2") + new String("2");
            t3.intern();
            String t4 = "22";
            System.out.println(t3 == t4);
        }

        {
            String t3 = new String("2") + "2";
            t3.intern();
            String t4 = "22";
            System.out.println(t3 == t4);
        }
        System.out.println("ok");
    }
}
