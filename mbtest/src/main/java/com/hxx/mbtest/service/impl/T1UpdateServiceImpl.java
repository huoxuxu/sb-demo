package com.hxx.mbtest.service.impl;

import com.hxx.mbtest.entity.T1;
import com.hxx.mbtest.mapper.T1Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-09 12:26:11
 **/
@Slf4j
@Service
public class T1UpdateServiceImpl {

    @Autowired
    private T1Mapper t1Mapper;

    public void updateUser() {
        log.info("开始执行：{}- {}", 1, new Date());
        T1 t1 = new T1();
        {
            t1.setId(11);

            t1.setName("苹果子1");
            t1.setScore(Double.valueOf(-1));
            t1.setCreateTime(LocalDateTime.now());
        }

        int ret = t1Mapper.updateUser(t1);
        System.out.println("updateUser：" + ret);

    }

}
