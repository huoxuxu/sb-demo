package com.hxx.sbweb.controller;

import com.github.pagehelper.PageInfo;
import com.hxx.sbweb.common.ResultHandler;
import com.hxx.sbweb.domain.User;
import com.hxx.sbweb.model.ResultBean;
import com.hxx.sbweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * mybatis 注解方式示例
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * http://localhost:8082/user/fenye?pageNum=1&pageSize=1
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/fenye")
    public ResultBean<List<User>> fenye(int pageNum,int pageSize) {
        PageInfo pageInfo = userService.fenye(pageNum, pageSize);
        return ResultHandler.ok(pageInfo);
    }

    /**
     * http://localhost:8082/user/list
     *
     * @return
     */
    @RequestMapping("/list")
    public ResultBean<List<User>> listAll() {
        List<User> users = userService.listAll();
        return ResultHandler.ok(users);
    }

    /**
     * http://localhost:8082/user/get?name=123
     *
     * @param name
     * @return
     */
    @RequestMapping("/get")
    public ResultBean<User> get(@RequestParam(value = "name") String name) {
        User u = userService.selectUser(name);
        return ResultHandler.ok(u);
    }

    /**
     * http://localhost:8082/user/in?ids=1,2,3,,5
     *
     * @param ids
     * @return
     */
    @RequestMapping("/in")
    public ResultBean<User> in(@RequestParam(value = "ids") String ids) {
        String[] arr = ids.split(",");
        List<Integer> idArr = Stream.of(arr)
                .filter(d -> d != null && !d.isEmpty())
                .map(d -> Integer.parseInt(d))
                .collect(Collectors.toList());

        List<User> uls = userService.selectUserIn(idArr);
        return ResultHandler.ok(uls);
    }

    /**
     * http://localhost:8082/user/like?name=h
     * @param name
     * @return
     */
    @RequestMapping("/like")
    public ResultBean<User> like(@RequestParam(value = "name") String name) {
        List<User> uls = userService.selectUserLike(name);
        return ResultHandler.ok(uls);
    }

    /**
     * http://localhost:8082/user/dynamic?name=1&age=22
     * @param name
     * @param age
     * @return
     */
    @RequestMapping("/dynamic")
    public ResultBean<User> dynamic(@RequestParam(value = "name") String name, @RequestParam(value = "age") Integer age) {
        List<User> uls = userService.selectUserDynamic(name, age);
        return ResultHandler.ok(uls);
    }




}
