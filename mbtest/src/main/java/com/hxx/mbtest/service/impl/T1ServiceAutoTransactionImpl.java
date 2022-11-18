package com.hxx.mbtest.service.impl;

import com.hxx.mbtest.entity.T1;
import com.hxx.mbtest.mapper.T1Mapper;
import com.hxx.mbtest.mapper.example.T1Example;
import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-09 12:26:11
 **/
@Slf4j
@Service
public class T1ServiceAutoTransactionImpl {
    @Autowired
    private T1Mapper t1Mapper;

    /**
     * 默认回滚 RuntimeException and Error
     *
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertM() throws Exception {
        T1 t1 = new T1();
        {
            t1.setCode("orange");
            t1.setName("橙子");
            t1.setScore(86.2);
            t1.setEnabled(true);
            t1.setEnabled(false);
            LocalDateTime now = LocalDateTime.now();
            t1.setBirthday(now.minusYears(1));
            t1.setCreateTime(now.minusYears(2));
        }

        int ret = t1Mapper.addUser(t1);
        System.out.println("ccc:" + ret);
//        OftenUtil.assertCond(true, "1231");
        throw new IOException("ioerr");
    }
}
