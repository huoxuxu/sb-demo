package com.hxx.sbcommon.common.io.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * jacksonStreamApi
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-09 12:57:57
 **/
public class JacksonReaderQuick {

    /**
     * 文件转Reader
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static FileReader getFileReader(File file) throws FileNotFoundException {
        return new FileReader(file);
    }

    /**
     * 字符串转Reader
     *
     * @param jsonString
     * @return
     */
    public static StringReader getStringReader(String jsonString) {
        return new StringReader(jsonString);
    }

    /**
     * 获取其它Reader流,推荐http响应流等等
     *
     * @param inputStream 流
     * @return
     */
    public static Reader getHttpResponseReader(InputStream inputStream) {
        return new InputStreamReader(inputStream);
    }

    /**
     * 使用jsonParser 提取数据并写入中间文件
     * 不支持字段为对象或数组的情况，eg.{[{"a":{}}]}
     */
    public static int parse(String jsonFilePath, Consumer<Map<String, Object>> consumer) throws IOException {
        int total = 0;
        JsonFactory jasonFactory = new JsonFactory();
        // {"data":[{"id":0}]}
        File jsonFile = new File(jsonFilePath);
        FileReader jsonFileReader = getFileReader(jsonFile);
        try (JsonParser parser = jasonFactory.createParser(jsonFileReader)) {
            while (!parser.isClosed()) {
                JsonToken jsonToken = parser.nextToken();

                if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                    String fieldName = parser.getCurrentName();
                    if ("data".equals(fieldName)) {
                        jsonToken = parser.nextToken();
                        if (JsonToken.START_ARRAY.equals(jsonToken)) {
                            while (!JsonToken.END_ARRAY.equals(parser.nextToken())) {
                                if (JsonToken.START_OBJECT.equals(parser.currentToken())) {
                                    // 开始读取对象
                                    Map<String, Object> objMap = new HashMap<>();
                                    while (true) {
                                        jsonToken = parser.nextToken();
                                        if (JsonToken.END_OBJECT.equals(jsonToken)) {
                                            // 读到对象结尾
                                            total++;
                                            //System.out.println(String.join(", ",objFields));
                                            break;
                                        }

                                        fieldName = parser.getCurrentName();
                                        jsonToken = parser.nextToken();
                                        Object v = parser.getText();
                                        switch (jsonToken) {
                                            case START_OBJECT:
                                            case END_OBJECT:
                                            case START_ARRAY:
                                            case END_ARRAY:
                                                throw new IllegalStateException("不支持对象属性包含对象或集合. 属性：" + fieldName);
                                            case VALUE_NULL:
                                                break;
                                            case VALUE_TRUE:
                                            case VALUE_FALSE:
                                                v = parser.getBooleanValue();
                                                break;
                                            case VALUE_NUMBER_INT:
                                                v = parser.getNumberValue();
                                                break;
                                            case VALUE_NUMBER_FLOAT:
                                                // 不能使用 getDoubleValue或getFloatValue 取值，会丢失小数点后的精度
                                                v = parser.getDecimalValue();
                                                break;
                                            case VALUE_STRING:
                                            default:
                                                v = parser.getText();
                                                break;
                                        }
                                        objMap.put(fieldName, v);
                                        System.out.println(fieldName + " : (" + jsonToken + ")" + v);
                                    }

                                    consumer.accept(objMap);
                                }
                            }
                        }
                    } else {
                    }
                }
            }
        }
        return total;
    }

//    @lombok.Data
//    public static class JsonValCls{
//        private
//    }


}
