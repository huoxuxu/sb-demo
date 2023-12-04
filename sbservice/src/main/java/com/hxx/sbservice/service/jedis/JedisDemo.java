package com.hxx.sbservice.service.jedis;

import com.hxx.sbcommon.common.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-04 12:39:37
 **/
@Slf4j
public class JedisDemo {

    public static void main(String[] args) {
        try {
            try (Jedis jedis = getJedis()) {
                demo(jedis);

            }
        } catch (Exception ex) {
            System.out.println(ex + "");
        }
    }

    static void demo(Jedis jedis) throws IOException {
        // jedis 所有的命令就是我们之前的所有指令
        String pingResult = jedis.ping();
        System.out.println(pingResult);

    }

    static void case1(Jedis jedis) {
        jedis.flushDB();//清空当前库的所有数据
        jedis.set("name", "111");
        jedis.set("high", "173");
        System.out.println("name:" + jedis.get("name") + "\nage:" + jedis.get("age") + "\nhigh" + jedis.get("high"));
    }

    static Jedis getJedis() throws IOException {
        File localCfgFile = new File("d:/tmp/local-config.json");
        String json = FileUtils.readFileToString(localCfgFile, StandardCharsets.UTF_8);
        Map localCfg = JsonUtil.parse(json, Map.class);
        String pwd = localCfg.getOrDefault("redis-ppp", "") + "";

        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.auth(pwd);
        return jedis;
    }
}
