package com.hxx.sbcommon.module.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单轮询
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 16:24:45
 **/
public class RoundRobinBalance {
    private AtomicInteger atomicIndex = new AtomicInteger(0);

    public <T> T doSelect(List<T> serverList) {
        // atomicIndex自增大于服务器数量时取余
        int index = atomicIndex.getAndIncrement() % serverList.size();
        return serverList.get(index);
    }
}
