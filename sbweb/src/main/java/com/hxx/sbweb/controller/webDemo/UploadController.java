package com.hxx.sbweb.controller.webDemo;

import com.hxx.sbweb.common.ResultHandler;
import com.hxx.sbweb.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
