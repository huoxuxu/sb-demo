package com.hxx.sbcommon.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * FastJson的相关方法
 */
@Slf4j
public class JsonUtil {
    private static SerializeConfig config;

    static {
        config = new SerializeConfig();
//		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormatSerializer df = new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss");
        config.put(java.util.Date.class, df);
        config.put(java.sql.Date.class, df);
        config.put(Timestamp.class, df);
    }

    private static final SerializerFeature[] FEATURES = {SerializerFeature.WriteMapNullValue,// 输出空置字段
            SerializerFeature.WriteNullListAsEmpty,// list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero,// 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse,// Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null，输出为""，而不是null
            SerializerFeature.DisableCircularReferenceDetect //禁止循环引用检测,如果重用对象的话，会使用引用的方式进行引用对象
    };

    /**
     * 序列化数据
     *
     * @param object
     * @return
     */
    public static String toJSON(Object object) {
        return JSON.toJSONString(object, FEATURES);
    }

    /**
     * 反序列化流
     *
     * @param inputStream
     * @param cls
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T parseStream(InputStream inputStream, Class<T> cls) throws IOException {
        return JSON.parseObject(inputStream, cls);
    }

    /**
     * 反序列化流
     *
     * @param inputStream
     * @param charset
     * @param cls
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T parseStream(InputStream inputStream, Charset charset, Class<T> cls) throws IOException {
        return JSON.parseObject(inputStream, charset, cls);
    }

    /**
     * 反序列化数据
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T parse(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 解析json为Map
     *
     * @param json
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> parseMap(String json) {
        return JSONObject.parseObject(json, new TypeReference<HashMap<K, V>>() {
        });
    }


    /***
     * 反序列化数据
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T parseNoThrow(String json, Class<T> clazz) {
        T t = null;
        try {
            t = JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("JSON反序列化异常：{}", json, e);
        }
        return t;
    }

    /***
     * 反序列化数据
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /***
     * 反序列化数据
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> parseArrayNoThrow(String json, Class<T> clazz) {
        List<T> list = null;
        try {
            list = JSON.parseArray(json, clazz);
        } catch (Exception e) {
            log.error("JSON反序列化异常：{}", json, e);
        }
        return list;
    }
}
