package com.hxx.sbcommon.common.io.json.jsonparser;

import com.hxx.sbcommon.common.io.json.jsonparser.common.util.BeautifyJsonUtils;
import com.hxx.sbcommon.common.io.json.jsonparser.common.exception.JsonTypeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by code4wt on 17/5/19.
 */
public class JsonArray implements Iterable<Object> {

    private final List<Object> list = new ArrayList<>();

    public void add(Object obj) {
        list.add(obj);
    }

    public Object get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    /**
     *
     * @param index
     * @return
     */
    public JsonObject getJsonObject(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JsonObject)) {
            throw new JsonTypeException("Type of value is not JsonObject");
        }

        return (JsonObject) obj;
    }

    /**
     *
     * @param index
     * @return
     */
    public JsonArray getJsonArray(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JsonArray)) {
            throw new JsonTypeException("Type of value is not JsonArray");
        }

        return (JsonArray) obj;
    }

    @Override
    public String toString() {
        return BeautifyJsonUtils.beautify(this);
    }

    public Iterator<Object> iterator() {
        return list.iterator();
    }
}
