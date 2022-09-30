package com.hxx.mbtest.service.impl.db.trans;

import com.hxx.mbtest.entity.T1;
import com.hxx.mbtest.mapper.T1Mapper;
import com.hxx.sbcommon.common.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-09-29 17:09:22
 **/
@Slf4j
@Service
public class T1TransServiceImpl {
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    private T1Mapper t1Mapper;

    public boolean transDemo(int flag) {
        // 模拟出现了异常，如何来进行处理？
        transactionTemplate.execute((transactionStatus -> {
            List<T1> t1s = t1Mapper.selectAll();
            T1 t1 = t1s.get(0);
            {
                t1.setName("橙子1");
                LocalDateTime now = LocalDateTime.now();
                t1.setCreateTime(now);
            }
            int ret = t1Mapper.updateUser(t1);

            if (flag == 1) {
                throw new IllegalStateException("123");
            }

            return transactionStatus;
        }));
        return true;
    }


    // 声明式事务
    public void transactionDemo() {
        // 手动回滚
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

        // 依靠Spring回滚事务
        {
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
