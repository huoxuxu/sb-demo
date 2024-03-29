package com.hxx.tkMybatisTest.service;

import com.hxx.tkMybatisTest.dal.mysql.testDB.entity.T1;
import com.hxx.tkMybatisTest.dal.mysql.testDB.entity.T1Example;
import com.hxx.tkMybatisTest.dal.mysql.testDB.mapper.T1Mapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-11-12 15:28:33
 **/
@Service
public class T1Service {
    @Autowired
    private T1Mapper t1Mapper;


    public List<T1> getAll(String data) {
        List<T1> t1s = t1Mapper.selectAll();
        return t1s;
    }

    public List<T1> selectBy(T1Example t1) {
        List<T1> t1s = t1Mapper.selectBy(t1);
        return t1s;
    }

    public Integer insertDemo(List<T1> t1s) {
        if (t1s.size() == 1) {
            return t1Mapper.insertOne(t1s.get(0));
        } else {
            return t1Mapper.insertList(t1s);
        }
    }

    public Integer updateDemo(T1 t1) {
        return t1Mapper.updateById(t1);
    }

    public Integer deleteDemo(List<Integer> ids) {
        return t1Mapper.delIn(ids);
    }

}
