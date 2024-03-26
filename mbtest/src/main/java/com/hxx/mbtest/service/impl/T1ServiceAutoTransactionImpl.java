package com.hxx.mbtest.service.impl;

import com.hxx.mbtest.entity.T1;
import com.hxx.mbtest.mapper.T1Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

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
            t1.setGmtCreate(now.minusYears(2));
        }

        int ret = t1Mapper.addUser(t1);
        System.out.println("ccc:" + ret);
//        OftenUtil.assertCond(true, "1231");
        throw new IOException("ioerr");
    }
}
