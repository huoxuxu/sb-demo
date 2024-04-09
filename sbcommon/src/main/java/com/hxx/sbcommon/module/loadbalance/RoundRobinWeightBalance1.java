package com.hxx.sbcommon.module.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单加权轮询-未测
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 16:26:38
 **/
public class RoundRobinWeightBalance1 {
    private AtomicInteger atomicIndex = new AtomicInteger(0);

    public <T extends MyServer> T doSelect(List<T> serverList) {
        int totalWeight = 0;
        int firstWeight = serverList.get(0).getWeight();
        boolean sameWeight = true;
        for (T server : serverList) {
            totalWeight += server.getWeight();
            if (sameWeight && server.getWeight() != firstWeight) {
                sameWeight = false;
            }
        }
        if (!sameWeight) {
            // 自增方式计算offset
            int offset = atomicIndex.getAndIncrement() % totalWeight;
            for (T server : serverList) {
                if (offset < server.getWeight()) {
                    return server;
                }
                offset -= server.getWeight();
            }
        }
        int index = atomicIndex.getAndIncrement() % serverList.size();
        return serverList.get(index);
    }

}
