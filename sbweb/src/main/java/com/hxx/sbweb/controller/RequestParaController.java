package com.hxx.sbweb.controller;

import com.hxx.sbweb.common.ResultHandler;
import com.hxx.sbweb.domain.User;
import com.hxx.sbweb.model.ResultBean;
import com.hxx.sbweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 请求参数映射示例
 */
@RestController
@RequestMapping("/reqpara")
public class RequestParaController {

    /**
     * 请求参数名和方法参数名不一致
     * http://localhost:8082/reqpara/getname?name=jay
     *
     * @param str
     * @return
     */
    @GetMapping("/getname")
    public String getname(@RequestParam(value = "name") String str) {
        return "name is:" + str;
    }

    /**
     * 请求参数名和方法参数名一致
     * http://localhost:8082/reqpara/getname2?str=jay
     *
     * @param str
     * @return
     */
    @GetMapping("/getname2")
    public String getname2(@RequestParam String str) {
        return "name is:" + str;
    }

    /**
     * 请求参数名和方法名不一致且包含默认值
     * http://localhost:8082/reqpara/get_default
     *
     * @param str
     * @return
     */
    @GetMapping("/get_default")
    public String getnameWithDefaultParam(@RequestParam(value = "name", defaultValue = "sx1999") String str) {
        return "name is:" + str;
    }

    /**
     * 请求ContentType必须是Json
     * http://localhost:8082/reqpara/reqJson
     * {"id":99,"name":"sx1999"}
     *
     * @param u
     * @return
     */
    @RequestMapping("/reqJson")
    public String reqJson(@RequestBody User u) {
        return u.getId() + "_" + u.getName();
    }

    /**
     * 映射URLPath参数
     * http://localhost:8082/reqpara/id/199
     *
     * @param id
     * @return
     */
    @RequestMapping("/id/{id}")
    public String id(@PathVariable("id") Integer id) {
        return "id:" + id;
    }


}
