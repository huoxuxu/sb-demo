package com.hxx.tkMybatisTest.service;

import com.hxx.tkMybatisTest.dal.mysql.testDB.entity.T1;
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

    public List<T1> selectBy(String code, String name) {
        T1 t1 = new T1();
        if (!StringUtils.isEmpty(code)) {
            t1.setCode(code);
        }
        if (!StringUtils.isEmpty(name)) {
            t1.setName(name);
        }

        List<T1> t1s = t1Mapper.selectBy(t1);
        return t1s;
    }

}
