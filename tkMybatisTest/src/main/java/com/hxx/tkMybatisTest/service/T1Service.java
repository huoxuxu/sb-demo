package com.hxx.tkMybatisTest.service;

import com.hxx.tkMybatisTest.dal.mysql.testDB.entity.T1;
import com.hxx.tkMybatisTest.dal.mysql.testDB.mapper.T1Mapper;
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

}
