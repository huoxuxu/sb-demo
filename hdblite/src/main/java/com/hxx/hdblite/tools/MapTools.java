package com.hxx.hdblite.tools;

import java.util.Map;

/**
 *
 */
public class MapTools {

    /**
     * 忽略大小写的Key
     *
     * @param map
     * @param key
     * @param <V>
     * @return
     */
    public static <V> boolean containsKey(Map<String, V> map, String key) {
        for (Map.Entry<String, V> item : map.entrySet()) {
            if (StringTools.EqualsIgnoreCase(item.getKey(), key)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取值，忽略Key的大小写
     *
     * @param map
     * @param key
     * @param <V>
     * @return
     * @throws Exception
     */
    public static <V> V getVal(Map<String, V> map, String key) throws Exception {
        for (Map.Entry<String, V> item : map.entrySet()) {
            if (StringTools.EqualsIgnoreCase(item.getKey(), key)) {
                return item.getValue();
            }
        }

        throw new Exception("字典中未找到对应的key【" + key + "】");
    }

}
