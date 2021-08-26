package com.hxx.sbrest.model;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-30 17:06:24
 **/
public class DataObject {
    private final String data;

    private static int objectCounter = 0;

    public DataObject(String str) {
        data = str;
    }

    public static DataObject get(String data) {
        objectCounter++;
        return new DataObject(data + "_" + objectCounter);
    }

    public String show() {
        return data;
    }
}
