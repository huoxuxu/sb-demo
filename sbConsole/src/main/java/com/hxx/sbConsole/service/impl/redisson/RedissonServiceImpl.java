//package com.hxx.sbConsole.service.impl.redisson;
//
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @Author: huoxuxu
// * @Description:
// * @Date: 2022-10-16 15:33:43
// **/
//@Slf4j
//@Service
//public class RedissonServiceImpl {
//    @Autowired
//    private RedissonClient redissonClient;
//
//
//    public void demo() throws InterruptedException {
//        RLock lock = getLock("keydemo");
//        try {
//            if (lock.tryLock(2, 30, TimeUnit.SECONDS)) {
//                // 获取到锁
//
//            } else {
//                // 未获取到锁
//
//            }
//        } finally {
//            if (lock != null && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }
//
//    private RLock getLock(String key) {
//        return redissonClient.getLock("redissonKEY:" + key);
//    }
//
//}
