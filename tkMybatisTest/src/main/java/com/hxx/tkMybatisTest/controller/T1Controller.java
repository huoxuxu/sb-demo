package com.hxx.tkMybatisTest.controller;

import com.hxx.sbcommon.common.JsonUtil;
import com.hxx.tkMybatisTest.dal.mysql.testDB.entity.T1;
import com.hxx.tkMybatisTest.service.T1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-11-12 15:29:10
 **/
@RequestMapping("t1")
@RestController
public class T1Controller {
    @Autowired
    private T1Service t1Service;


    @RequestMapping("get")
    public String get(String data) {
        List<T1> all = t1Service.getAll(data);
        return JsonUtil.toJSON(all);
    }

}
