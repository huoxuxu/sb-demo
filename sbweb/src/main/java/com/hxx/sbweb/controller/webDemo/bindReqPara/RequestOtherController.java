package com.hxx.sbweb.controller.webDemo.bindReqPara;

import com.hxx.sbweb.controller.base.BaseRestController;
import com.hxx.sbweb.domain.Book;
import com.hxx.sbweb.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-04 16:57:36
 **/
@Slf4j
@RestController
@RequestMapping("/reqother")
public class RequestOtherController extends BaseRestController {

    @RequestMapping("/testHeaderParam")
    public void testHeaderParam(@RequestHeader String param1) {
        System.out.println("通过RequestHeader获取的参数param1=" + param1);
    }

    @RequestMapping("/testCookieParam")
    public void testCookieParam(HttpServletRequest request, HttpServletResponse response, @CookieValue String sessionid) {
        System.out.println("通过CookieValue获取的参数sessionid=" + sessionid);
    }

    @RequestMapping("/testHttpEntity")
    public ResponseEntity<String> handle(HttpEntity<byte[]> requestEntity) {
        String requestHeader = requestEntity.getHeaders()
                .getFirst("MyRequestHeader");
        byte[] requestBody = requestEntity.getBody();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("MyResponseHeader", "MyValue");
        return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
    }

    // InitBind=============================================================

    /**
     * localhost:8082/reqpara/hello?user.name=hangge&user.age=11&book.name=ThinkingInJava&book.price=56.5
     *
     * @param user
     * @param book
     * @return
     */
    @GetMapping("/hello")
    public String hello(@ModelAttribute("user") User user,
                        @ModelAttribute("book") Book book) {
        return "name：" + user.getName() + " | age：" + user.getAge() + "<br>" + "name：" + book.getName() + " | price：" + book.getPrice();
    }

}
