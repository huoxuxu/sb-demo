package com.hxx.sbweb.controller.webDemo.bindReqPara;

import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbcommon.common.web.HttpServletRequestUtil;
import com.hxx.sbcommon.model.HttpMultipartFormDataInfo;
import com.hxx.sbweb.controller.base.BaseRestController;
import com.hxx.sbweb.domain.Book;
import com.hxx.sbweb.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-04 16:57:36
 **/
@Slf4j
@RestController
@RequestMapping("/reqother")
public class RequestOtherController extends BaseRestController {

    /**
     * /reqother/all
     *
     * @param request
     * @param response
     */
    @RequestMapping("/all")
    public Map<String, Object> all(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Map<String, String> headerMap = HttpServletRequestUtil.getHeaderMap(request);

        // body 只能读取一次body
        String body = HttpServletRequestUtil.getRequestBody(request);
        // multipart
        List<HttpMultipartFormDataInfo> files = HttpServletRequestUtil.getMultipartFormData(request);

        Map<String, Object> map = new HashMap<>();
        map.put("requestURL", request.getRequestURL());
        map.put("requestURI", request.getRequestURI());
        map.put("queryString", request.getQueryString());
        map.put("headers", headerMap);
        map.put("body", body);
        map.put("files", files);
        map.put("cookie", headerMap.getOrDefault("cookie", null));

        return map;
    }

    /**
     * /reqother/header
     *
     * @param hk1
     */
    @RequestMapping("/header")
    public String testHeaderParam(@RequestHeader String hk1, @CookieValue String sessionid) {
        Map<String, Object> map = new HashMap<>();
        map.put("cookie", "");
        map.put("sessionid", sessionid);
        map.put("hk1", hk1);
        return JsonUtil.toJSON(map);
    }

    /**
     * CookieValue
     *
     * @param request
     * @param response
     * @param sessionid
     */
    @RequestMapping("/cookie")
    public void cookie(HttpServletRequest request, HttpServletResponse response, @CookieValue String sessionid) {
        System.out.println("通过CookieValue获取的参数sessionid=" + sessionid);
    }

    @RequestMapping("/cookie2")
    public void cookie2(HttpServletRequest request, HttpServletResponse response, @CookieValue("token") String
            token) {
        System.out.println("通过CookieValue获取的参数sessionid=" + token);
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
