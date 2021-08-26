package com.hxx.mbmtest.mapper.master;

import com.hxx.mbmtest.entity.T1;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-09 12:01:53
 **/
public interface T1Mapper {
    // 查询全部
    List<T1> selectAll();

    //通过ID查询用户
    T1 selectUserById(@Param("id") int id);

    T1 selectUser(@Param("name") String name);

    List<T1> selectByBirthday(@Param("birthday") LocalDateTime birthday,
                              @Param("page") Integer page,
                              @Param("pageSize") Integer pageSize);

    //Map 映射
    List<T1> selectUserByMap(Map map);

    //添加用户
    int addUser(T1 user);

    //修改用户信息
    int updateUser(T1 user);

    //删除用户
    int deleteUserById(@Param("id") int id);


}
