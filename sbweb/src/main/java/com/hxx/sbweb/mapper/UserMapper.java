package com.hxx.sbweb.mapper;

import com.hxx.sbweb.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface  UserMapper {
    /**
     *
     * @return
     */
    @Select("SELECT * FROM users")
    @Results({
            @Result(property = "createTime",  column = "CreateTime")
    })
    List<User> selectAll();

    //通过ID查询用户
    @Select("select * from users where id = #{id}")
    User selectUserById(@Param("id") int id);

    //通过ID in方式查询用户
    @Select({
            "<script>",
            "select * from users",
            "where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<User> selectUserIn(@Param("ids") List<Integer> ids);

    @Select("select * from users where name = #{name}")
    User selectUser(@Param("name") String name);

    /**
     * like 模糊查询
     * 注意：传值时需要加上%
     * @param name 示例：name="%"+name+"%"
     * @return
     */
    @Select("select * from users where name like #{name}")
    List<User> selectUserLike(@Param("name") String name);

    //动态SQL查询
    @Select("<script> \n"+
            "select * from users " +
            "where " +
            "1=1 " +
            "and name like #{name} " +
            "<if test='age > 18'> and age =#{age} </if>" +
            "</script>")
    List<User> selectUserDynamic(@Param("name") String name, @Param("age") Integer age);

    //动态SQL查询2
    @Select("<script> \n"+
            "select * from users " +
            "where name like #{name} " +
            "<if test='age > 18'> and age =#{age} </if>" +
            "</script>")
    List<User> selectUserDynamic2(@Param("name") String name, @Param("age") String age);



    //添加用户
    @Insert("insert into users (id,name,createTime) values (#{id},#{name},#{createTime})")
    int addUser(User user);

    //修改用户信息
    @Update("update users set name = #{name},createTime = #{createTime} where id = #{id}")
    int updateUser(User user);

    //删除用户
    @Delete("delete from users where id=#{id}")
    int deleteUserById(@Param("id") int id);



}
