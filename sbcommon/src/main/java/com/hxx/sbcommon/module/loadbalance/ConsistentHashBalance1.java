package com.hxx.sbcommon.module.loadbalance;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 16:30:19
 **/
public class ConsistentHashBalance1 {
    private ConsistentHashSelector consistentHashSelector;

    public MyServer doSelect(String clientIP, List<MyServer> serverList) {
        initialConsistentHashSelector(serverList);
        return consistentHashSelector.select(clientIP);
    }

    private class ConsistentHashSelector {
        private Integer identityHashCode;
        private TreeMap<Integer, MyServer> serverNodes = new TreeMap<>();

        // 构建哈希环
        public ConsistentHashSelector(Integer identityHashCode, List<MyServer> serverList) {
            this.identityHashCode = identityHashCode;
            TreeMap<Integer, MyServer> newServerNodes = new TreeMap<Integer, MyServer>();
            for (MyServer server : serverList) {
                newServerNodes.put(hashCode(server.getIp()), server);
            }
            serverNodes = newServerNodes;
        }

        // 根据客户端IP路由
        public MyServer select(String clientIP) {

            // 计算客户端哈希值
            int clientHashCode = hashCode(clientIP);

            // 找到第一个大于客户端哈希值的服务器
            SortedMap<Integer, MyServer> tailMap = serverNodes.tailMap(clientHashCode, false);
            if (tailMap == null || tailMap.isEmpty()) {
                Integer firstKey = serverNodes.firstKey();
                return serverNodes.get(firstKey);
            }

            // 找不到表示在最后一个节点和第一个节点之间 ->选择第一个节点
            Integer firstKey = tailMap.firstKey();
            return tailMap.get(firstKey);
        }

        // 计算哈希值
        private int hashCode(String key) {
            return Objects.hashCode(key);
        }

        // 提供者列表哈希值 -> 如果新增或者删除提供者会发生变化
        public Integer getIdentityHashCode() {
            return identityHashCode;
        }
    }

    private void initialConsistentHashSelector(List<MyServer> serverList) {

        // 计算提供者列表哈希值
        Integer newIdentityHashCode = System.identityHashCode(serverList);

        // 提供者列表哈希值没有变化则无需重新构建哈希环
        if (null != consistentHashSelector && (null != consistentHashSelector.getIdentityHashCode() && newIdentityHashCode == consistentHashSelector.getIdentityHashCode())) {
            return;
        }
        // 提供者列表哈希值发生变化则重新构建哈希环
        consistentHashSelector = new ConsistentHashSelector(newIdentityHashCode, serverList);
    }
}
