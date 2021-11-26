package com.hxx.tkMybatisTest.dal.mysql.testDB.mapper;

import com.hxx.tkMybatisTest.dal.mysql.testDB.entity.T1;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-09 12:01:53
 **/
@Component
@org.apache.ibatis.annotations.Mapper
public interface T1Mapper extends Mapper<T1> {

    List<T1> selectBy(T1 t1);

    Integer insertOne(T1 t1);
    Integer insertList(List<T1> t1s);

    Integer updateById(T1 t1);

    Integer delIn(List<Integer> ids);
}
