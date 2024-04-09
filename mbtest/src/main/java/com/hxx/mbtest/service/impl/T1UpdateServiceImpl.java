package com.hxx.mbtest.service.impl;

import com.hxx.mbtest.entity.T1;
import com.hxx.mbtest.mapper.T1Mapper;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
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

    public void run() {
        int cid = 0;
        {
            T1 t1 = t1Mapper.selectUser("橙子");
            if (t1 != null) {
                cid = t1.getId();
            }
            System.out.println("selectUser：");
            System.out.println(JsonUtil.toJSON(t1));

            {
                t1.setName("橙子1");
                LocalDateTime now = LocalDateTime.now();
                t1.setGmtCreate(now);
            }
            int ret = t1Mapper.updateUser(t1);
            System.out.println("updateUser：");
            System.out.println(ret);
        }
    }

    public void updateUser() {
        log.info("开始执行：{}- {}", 1, new Date());
        T1 t1 = new T1();
        {
            t1.setId(11);

            t1.setName("苹果子1");
            t1.setScore(Double.valueOf(-1));
            t1.setGmtCreate(LocalDateTime.now());
        }

        int ret = t1Mapper.updateUser(t1);
        System.out.println("updateUser：" + ret);
    }

}
