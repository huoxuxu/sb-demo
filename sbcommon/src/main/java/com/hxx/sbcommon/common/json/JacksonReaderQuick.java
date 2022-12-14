package com.hxx.sbcommon.common.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-09 12:57:57
 **/
public class JacksonReaderQuick {

    /**
     * 文件转Reader
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static FileReader getFileReader(String filePath) throws FileNotFoundException {
        return new FileReader(filePath);
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
     */
    public static int parse(String jsonFilePath) throws IOException {
        int total = 0;
        JsonFactory jasonFactory = new JsonFactory();
        // {"data":[{"id":0}]}
        try (JsonParser parser = jasonFactory.createParser(getFileReader(jsonFilePath))) {
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
                                    List<String> objFields = new ArrayList<>();
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
                                        String v = parser.getText();
                                        objFields.add(fieldName + " : " + v);
                                    }
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

}
