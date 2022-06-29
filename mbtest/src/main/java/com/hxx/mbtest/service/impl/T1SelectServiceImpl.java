package com.hxx.mbtest.service.impl;

import com.hxx.mbtest.entity.T1;
import com.hxx.mbtest.mapper.T1Mapper;
import com.hxx.mbtest.mapper.example.T1Example;
import com.hxx.mbtest.service.T1Service;
import com.hxx.sbcommon.common.JsonUtil;
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
@Service
public class T1SelectServiceImpl implements T1Service {
    private final static Logger log = LoggerFactory.getLogger(T1SelectServiceImpl.class);

    @Autowired
    private T1Mapper t1Mapper;

    // 编程式事务
    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    PlatformTransactionManager transactionManager;

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
            System.out.println("addUser：" + ret);
        }
        {
            T1 t1 = new T1();
            {
                t1.setCode("apple");
                t1.setName("苹果");
//                t1.setScore(86.2);
                t1.setEnabled(true);
                LocalDateTime now = LocalDateTime.now();
                t1.setBirthday(now.minusYears(1));
                t1.setCreateTime(now.minusYears(2));
            }
            int ret = t1Mapper.addUserDynamic(t1);
            System.out.println("addUserDynamic：" + ret);
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
            {
                T1Example example = new T1Example();
                example.setCode("renyingying1");
                example.addCode();
                List<T1> t1s = t1Mapper.selectByExample(example);
                System.out.println("selectByExample：" + JsonUtil.toJSON(example));
                System.out.println(JsonUtil.toJSON(t1s));
            }
            {
                T1Example example = new T1Example();
                List<Integer> ids = new ArrayList<>();
                ids.add(15);
                ids.add(1);
                ids.add(2);

                example.setIds(ids);
                example.setCode("renyingying1");
                example.setName("任盈盈");
                example.setEnabled(1);
                example.setBirthdayStart(LocalDateTime.of(2020, 5, 13, 0, 0));
                example.setBirthdayEnd(LocalDateTime.of(2020, 5, 14, 0, 0));

                // 排序
                example.setOrderByCase("id desc");
                // 分页
                example.setPageIndex(0);
                example.setPageSize(90);

                example.addIds().add11().addCode().addName().addEnabled().addBirthday();
                List<T1> t1s = t1Mapper.selectByExample(example);
                System.out.println("selectByExample：" + JsonUtil.toJSON(example));
                System.out.println(JsonUtil.toJSON(t1s));
            }
        }
        {
            int ret = t1Mapper.deleteUserById(cid);
            System.out.println("deleteUserById：" + cid + " ret:" + ret);
        }


    }

    @Override
    public void TransactionDemo() {
        transactionDemo();
    }

    public void updateBatch() {
        List<Map<String, Object>> ls = new ArrayList<>();
        for (int i = 13; i < 20; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i);
            map.put("name", "N" + i);

            ls.add(map);
        }
        t1Mapper.updateBatch(ls);
    }

    // 声明式事务
    private void transactionDemo() {
        //开启事务保存数据
        {
            transactionTemplate.execute((status) -> {
                try {
                    Integer cid = 10;
                    T1 t1 = t1Mapper.selectUserById(cid);
                    log.info("T1: " + JsonUtil.toJSON(t1));
                    int ret = t1Mapper.deleteUserById(cid);
                    System.out.println("deleteUserById：" + cid + " ret:" + ret);

                    return true;
                } catch (Exception e) {
                    //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    // 手动回滚事务
                    status.setRollbackOnly();
                    log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
                    return false;
                }
            });
        }

        {
            // 依靠Spring回滚事务
            transactionTemplate.execute((status) -> {
                Integer cid = 10;
                T1 t1 = t1Mapper.selectUserById(cid);
                log.info("T1: " + JsonUtil.toJSON(t1));
                int ret = t1Mapper.deleteUserById(cid);
                System.out.println("deleteUserById：" + cid + " ret:" + ret);

                return true;
            });
        }
    }

    // 声明式事务2
    public void transactionDemo2() {
        // 定义事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setReadOnly(false);
        //隔离级别,-1表示使用数据库默认级别
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            //TODO something

            // 手动提交
            transactionManager.commit(status);
        } catch (Exception e) {
            // 手动回滚
            transactionManager.rollback(status);
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }


}
