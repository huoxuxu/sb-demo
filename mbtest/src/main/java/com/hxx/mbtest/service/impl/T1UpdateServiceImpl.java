package com.hxx.mbtest.service.impl;

import com.hxx.mbtest.entity.T1;
import com.hxx.mbtest.mapper.T1Mapper;
import com.hxx.mbtest.mapper.example.T1Example;
import com.hxx.mbtest.service.T1Service;
import com.hxx.sbcommon.common.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

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
