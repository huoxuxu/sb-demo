package com.hxx.sbweb.controller;

import com.hxx.sbweb.common.ResultHandler;
import com.hxx.sbweb.domain.User;
import com.hxx.sbweb.model.ResultBean;
import com.hxx.sbweb.service.UserXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * mybatis xml配置SQL方式示例
 */
@RestController
@RequestMapping("/userxml")
public class UserXmlController {
    @Autowired
    private UserXmlService userService;

    /**
     * http://localhost:8082/userxml/list
     * @return
     */
    @RequestMapping("/list")
    public ResultBean<List<User>> selectAll() {
        List<User> users = userService.selectAll();
        return ResultHandler.ok(users);
    }

    /**
     * http://localhost:8082/userxml/get?name=123
     * @param name
     * @return
     */
    @RequestMapping("/get")
    public ResultBean<User> get(@RequestParam(value="name")String name){
        User u=userService.selectUser(name);
        return ResultHandler.ok(u);
    }

    /**
     * http://localhost:8082/userxml/bymap?min=0&max=2
     * @param min
     * @param max
     * @return
     */
    @RequestMapping("/bymap")
    public ResultBean<User> selectListByMap(@RequestParam(value = "min") Integer min,@RequestParam(value = "max") Integer max) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("min",min);
        map.put("max",max);

        List<User> uls = userService.selectUserByMap(map);
        return ResultHandler.ok(uls);
    }

    /**
     * http://localhost:8082/userxml/list2
     * @return
     */
    @RequestMapping("/list2")
    public ResultBean<User> listAll2() {
        List<User> uls = userService.selectAll2();
        return ResultHandler.ok(uls);
    }


}
