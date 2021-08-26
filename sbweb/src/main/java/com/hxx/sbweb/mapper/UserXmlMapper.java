package com.hxx.sbweb.mapper;

import com.hxx.sbweb.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface UserXmlMapper {

    List<User> selectAll();

    //通过ID查询用户
    User selectUserById(@Param("id") int id);

    User selectUser(@Param("name") String name);

    //Map 映射
    List<User> selectUserByMap(Map map);

    //多对一匹配
    List<User> selectAll2();


    //添加用户
    int addUser(User user);

    //修改用户信息
    int updateUser(User user);

    //删除用户
    int deleteUserById(@Param("id") int id);

}
