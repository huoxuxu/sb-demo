package com.hxx.sbweb.controller.webDemo.bindReqPara;

import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbcommon.model.Result;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-04 16:57:36
 **/
@Slf4j
@RestController
@RequestMapping("/routeBind")
public class RouteBindController extends BaseRestController {

    /**
     * /routeBind/t1/9998/hhhxxx
     *
     * @param id
     * @param name
     * @return
     */
    @GetMapping("t1/{id}/{name}")
    public String test5(@PathVariable("id") Long id, @PathVariable("name") String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);

        return JsonUtil.toJSON(map);
    }

}
