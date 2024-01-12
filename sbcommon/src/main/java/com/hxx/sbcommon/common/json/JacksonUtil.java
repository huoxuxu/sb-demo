package com.hxx.sbcommon.common.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-25 17:00:40
 **/
@Slf4j
public class JacksonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 对象转换成json
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String beanToJson(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("beanToJson error", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将JSON字符串根据指定的Class反序列化成Java对象。
     *
     * @param json      JSON字符串
     * @param pojoClass Java对象Class
     * @return 反序列化生成的Java对象
     * @throws Exception 如果反序列化过程中发生错误，将抛出异常
     */
    public static Object decode(String json, Class<?> pojoClass) throws Exception {
        return objectMapper.readValue(json, pojoClass);
    }

    /**
     * 将JSON字符串根据指定的Class反序列化成Java对象。
     *
     * @param json      JSON字符串
     * @param reference 类型引用
     * @return 反序列化生成的Java对象
     * @throws Exception 如果反序列化过程中发生错误，将抛出异常
     */
    public static Object decode(String json, TypeReference<?> reference) throws Exception {
        return objectMapper.readValue(json, reference.getClass());
    }

    /**
     * 将Java对象序列化成JSON字符串。
     *
     * @param obj 待序列化生成JSON字符串的Java对象
     * @return JSON字符串
     * @throws Exception 如果序列化过程中发生错误，将抛出异常
     */
    public static String encode(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * 对象转换成格式化的json
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String beanToJsonPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("beanToJsonPretty error {}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }


    /**
     * 将json转换成对象Class
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToBean(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class)
                    ? (T) str
                    : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.error("jsonToBean error {}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * 将json转换为对象集合
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToBeanList(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            JavaType javaType = getCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.error("jsonToBeanList error", e);
            return null;
        }
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
