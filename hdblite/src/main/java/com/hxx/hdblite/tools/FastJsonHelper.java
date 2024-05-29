package com.hxx.hdblite.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.Type;
import java.util.List;

public class FastJsonHelper {
    static {
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        TypeUtils.compatibleWithJavaBean = true;//fastjson会先判断propertyName长度大于1、且头两个字符都是大写时，不做转换
        TypeUtils.compatibleWithFieldName = true;//去除字段首字母大写时被转为小写的问题
        //首字母小写，第二字母大写时，需要将全部字段小写，然后使用@JSONField(name = “iPhone”)

        //		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        //JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
        //JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss.SSS");
        //JSON.toJSONString(obj, SerializerFeature.UseISO8601DateFormat);
    }

    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    public static String ToJson(Object obj) {
        return JSON.toJSONString(obj,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 反序列化为实体
     *
     * @param jsonStr
     * @param type    Type type = new TypeReference<List<JsonRootBean>>() {}.getType();
     * @return
     */
    public static <T> T DeSer(String jsonStr, Type type) {
        return JSON.parseObject(jsonStr, type);
    }

    /**
     * 反序列化为集合
     *
     * @param jsonStr
     * @param type    Type type = new TypeReference<List<JsonRootBean>>() {}.getType();
     * @return
     */
    public static <T> List<T> DeSerArr(String jsonStr, Type type) {
        List<T> list = JSON.parseObject(jsonStr, type);

        return list;
    }


}
