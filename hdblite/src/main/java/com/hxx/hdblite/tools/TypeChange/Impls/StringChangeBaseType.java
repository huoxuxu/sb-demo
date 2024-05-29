package com.hxx.hdblite.tools.TypeChange.Impls;

import com.hxx.hdblite.tools.TypeChange.IChangeBaseType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringChangeBaseType implements IChangeBaseType {
    private String val;

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    public StringChangeBaseType(String val) {
        this.val = val;
    }

    /// <summary>
    /// 忽略大小写的true 为true
    /// </summary>
    /// <returns></returns>
    public boolean ToBoolean() {
        return "true".equals(val.toLowerCase());
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public LocalDateTime ToDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(val, formatter);
        return dateTime;
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public double ToDouble() {
        return Double.parseDouble(val);
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public int ToInt32() {
        return Integer.parseInt(val);
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public long ToInt64() {
        return Long.parseLong(val);
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public String ToStr() {
        return val;
    }
}
