package com.hxx.sbcommon.common.json;

import com.alibaba.fastjson.JSONReader;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * JsonReader帮助类
 * 注意：如果jsonArray包含多个对象，如果只取一部分，则不要调用endArray()方法（报错）
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-03 21:30:16
 **/
@Slf4j
public class FastJsonReaderQuick implements Closeable {
    private final JSONReader reader;

    /**
     * 文件：getFileReader
     * 字符串：getStringReader
     *
     * @param r
     */
    public FastJsonReaderQuick(Reader r) {
        this.reader = new JSONReader(r);
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception ignore) {

            }
        }
    }

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

    public JSONReader getJSONReader(){
        return reader;
    }

    /**
     * 解析数组
     *
     * @param func map转实体 注意：返回null时会停止解析. p1 对象索引，p2 数据map，p3 返回生成的POJO
     * @param <T>
     */
    public <T> void parsePOJOArr(BiFunction<Integer, Map<String, Object>, T> func) {
        this.reader.startArray();

        boolean breakFlag = false;
        int ind = 0;
        while (this.reader.hasNext()) {
            T t = parsePOJO(ind, func);
            if (t == null) {
                breakFlag = true;
                break;
            }

            ind++;
        }
        if (!breakFlag) {
            this.reader.endArray();
        }
    }

    /**
     * 解析POJO
     *
     * @param func map转POJO p1 对象索引，p2 数据map，p3 返回生成的POJO
     * @param <T>
     * @return
     */
    public <T> T parsePOJO(BiFunction<Integer, Map<String, Object>, T> func) {
        return parsePOJO(0, func);
    }

    /**
     * 解析对象
     *
     * @param consumer
     */
    public void parsePOJO(BiConsumer<Integer, Map<String, Object>> consumer) {
        parsePOJO(0, consumer);
    }

    /**
     * 解析POJO
     *
     * @param ind  行索引
     * @param func map转POJO p1 对象索引，p2 数据map，p3 返回生成的POJO
     * @param <T>
     * @return
     */
    private  <T> T parsePOJO(int ind, BiFunction<Integer, Map<String, Object>, T> func) {
        this.reader.startObject();

        Map<String, Object> map = new HashMap<>();
        while (this.reader.hasNext()) {
            String k = this.reader.readString();
            Object v = this.reader.readObject();

            map.put(k, v);
        }
        this.reader.endObject();

        // 转为实体
        return func.apply(ind, map);
    }

    /**
     * 解析对象
     *
     * @param ind
     * @param consumer
     */
    private void parsePOJO(int ind, BiConsumer<Integer, Map<String, Object>> consumer) {
        this.reader.startObject();

        Map<String, Object> map = new HashMap<>();
        while (this.reader.hasNext()) {
            String k = this.reader.readString();
            Object v = this.reader.readObject();

            map.put(k, v);
        }
        this.reader.endObject();

        // 转为实体
        consumer.accept(ind, map);
    }


    // demo
    private void scanObjDemo() {
        this.reader.startObject();
        while (reader.hasNext()) {
            String key = reader.readString();
            System.out.print("key " + key);
            if (key.equals("array")) {
                reader.startArray();
                System.out.print("start " + key);
                while (reader.hasNext()) {
                    String item = reader.readString();
                    System.out.print(item);
                }
                reader.endArray();
                System.out.print("end " + key);
            } else if (key.equals("arraylist")) {
                reader.startArray();
                System.out.print("start " + key);
                while (reader.hasNext()) {
                    reader.startObject();
                    System.out.print("start arraylist item");
                    while (reader.hasNext()) {
                        String arrayListItemKey = reader.readString();
                        String arrayListItemValue = reader.readObject()
                                .toString();
                        System.out.print("key " + arrayListItemKey);
                        System.out.print("value " + arrayListItemValue);
                    }
                    reader.endObject();
                    System.out.print("end arraylist item");
                }
                reader.endArray();
                System.out.print("end " + key);
            } else if (key.equals("object")) {
                reader.startObject();
                System.out.print("start object item");
                while (reader.hasNext()) {
                    String objectKey = reader.readString();
                    String objectValue = reader.readObject()
                            .toString();
                    System.out.print("key " + objectKey);
                    System.out.print("value " + objectValue);
                }
                reader.endObject();
                System.out.print("end object item");
            } else if (key.equals("string")) {
                System.out.print("start string");
                String value = reader.readObject()
                        .toString();
                System.out.print("value " + value);
                System.out.print("end string");
            }
        }
        this.reader.endObject();
    }

}
