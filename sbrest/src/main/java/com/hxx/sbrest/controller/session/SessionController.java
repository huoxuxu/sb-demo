package com.hxx.sbrest.controller.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-10 14:10:04
 **/
@Slf4j
@RestController
@RequestMapping("session")
public class SessionController {

    /**
     * /session/reg
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/reg")
    public Map<String, String> register(HttpServletRequest req, Integer k) {
        HttpSession session = req.getSession();
        String sessionId = session.getId();
        session.setAttribute("k", k);

        Map<String, String> map = new HashMap<>();
        map.put("sessionId", sessionId);
        map.put("k", k + "");
        return map;
    }

    /**
     * /session/get
     *
     * @param req
     * @return
     */
    @GetMapping(value = "/get")
    public Map<String, String> get(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String sessionId = session.getId();
        Object k = session.getAttribute("k");

        Map<String, String> map = new HashMap<>();
        map.put("sessionId", sessionId);
        map.put("k", k + "");
        return map;
    }


}
