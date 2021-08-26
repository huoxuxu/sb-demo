package com.hxx.mbmtest.service.impl;

import com.hxx.mbmtest.entity.T1;
import com.hxx.mbmtest.mapper.master.T1Mapper;
import com.hxx.mbmtest.service.T1Service;
import com.hxx.sbcommon.common.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-09 12:26:11
 **/
@Service
public class T1ServiceImpl implements T1Service {
    private final static Logger log = LoggerFactory.getLogger(T1ServiceImpl.class);

    @Autowired
    private T1Mapper t1Mapper;

    @Override
    public void Run() {
        log.info("开始执行：{}- {}", 1, new Date());
        {
            List<T1> t1s = t1Mapper.selectAll();
            System.out.println("selectAll：");
            System.out.println(JsonUtil.toJSON(t1s));
        }
        {
            T1 t1 = t1Mapper.selectUserById(1);
            System.out.println("selectUserById：");
            System.out.println(JsonUtil.toJSON(t1));
        }
        {
            LocalDateTime now = LocalDateTime.now();

            int batchSize = 10;
            for (int i = 0; i < Integer.MAX_VALUE; i += batchSize) {
                List<T1> t1s = t1Mapper.selectByBirthday(now, i, batchSize);

                System.out.println("selectByBirthday page=" + i + " pageSize=" + batchSize + "：");
                if (CollectionUtils.isEmpty(t1s)) {
                    break;
                }

                System.out.println(JsonUtil.toJSON(t1s));
            }

        }
        {
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
            System.out.println("addUser：");
            System.out.println(ret);
        }

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
                t1.setCreateTime(now);
            }
            int ret = t1Mapper.updateUser(t1);
            System.out.println("updateUser：");
            System.out.println(ret);
        }
        {
            Map<String, Integer> map = new HashMap<>();
            map.put("min", 0);
            map.put("max", 15);
            List<T1> t1s = t1Mapper.selectUserByMap(map);
            System.out.println("selectUserByMap：");
            System.out.println(JsonUtil.toJSON(t1s));
        }
        {
            int ret = t1Mapper.deleteUserById(cid);
            System.out.println("deleteUserById：" + cid);
            System.out.println(ret);
        }


    }


}
