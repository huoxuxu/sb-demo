package com.hxx.sbweb.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hxx.sbweb.domain.User;
import com.hxx.sbweb.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public PageInfo fenye(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.selectAll();
        PageInfo page=new PageInfo(users);
        return page;
    }

    public List<User> listAll() {
        return userMapper.selectAll();
    }

    public User selectUserById(int id) {
        return userMapper.selectUserById(id);
    }

    /**
     * in
     * @param ids
     * @return
     */
    public List<User> selectUserIn(List<Integer> ids) {
        return userMapper.selectUserIn(ids);
    }

    public User selectUser(String name) {
        return userMapper.selectUser(name);
    }

    /**
     * like
     * @param name
     * @return
     */
    public List<User> selectUserLike(String name) {
        name = "%" + name + "%";
        return userMapper.selectUserLike(name);
    }

    /**
     * dynamic
     * @param name
     * @param age
     * @return
     */
    public List<User> selectUserDynamic(String name, Integer age) {
        name = "%" + name + "%";
        return userMapper.selectUserDynamic(name, age);
    }



}
