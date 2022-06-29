package com.hxx.mbtest.mapper;

import com.hxx.mbtest.entity.T1;
import com.hxx.mbtest.mapper.example.T1Example;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-09 12:01:53
 **/
@Component
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

    // 通用查询
    List<T1> selectByExample(T1Example example);

    // Count
    int count();

    //添加用户
    int addUser(T1 user);

    // 添加用户，动态列
    int addUserDynamic(T1 user);

    // 批量添加，并给list中元素的id属性赋值
    int insertBatch(List<T1> list);

    //修改用户信息
    int updateUser(T1 user);

    // 批量更新
    void updateBatch(List<Map<String, Object>> ls);

    //删除用户
    int deleteUserById(@Param("id") int id);


}
