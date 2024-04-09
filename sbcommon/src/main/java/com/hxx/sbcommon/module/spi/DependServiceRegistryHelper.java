package com.hxx.sbcommon.module.spi;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:44:58
 **/
public class DependServiceRegistryHelper {
    /**
     * 存储策略依赖的服务,统一管理
     */
    private static Map<String, Object> dependManagerMap = Maps.newHashMap();


    public static boolean registryMap(Map<Class, Object> dependManagerMap) {
        for (Map.Entry<Class, Object> dependEntry :
                dependManagerMap.entrySet()) {
            registry(dependEntry.getKey(), dependEntry.getValue());
        }
        return true;
    }


    public static boolean registry(Class cls, Object dependObject) {
        dependManagerMap.put(cls.getCanonicalName(), dependObject);
        return true;
    }


    public static Object getDependObject(Class cls) {


        return dependManagerMap.get(cls.getCanonicalName());
    }
}
