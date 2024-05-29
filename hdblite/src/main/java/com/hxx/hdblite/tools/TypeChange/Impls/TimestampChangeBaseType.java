package com.hxx.hdblite.tools.TypeChange.Impls;

import com.hxx.hdblite.tools.TypeChange.IChangeBaseType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampChangeBaseType implements IChangeBaseType {
    private Timestamp val;

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    public TimestampChangeBaseType(Timestamp val) {
        this.val = val;
    }

    /// <summary>
    /// 忽略大小写的true 为true
    /// </summary>
    /// <returns></returns>
    public boolean ToBoolean() {
        return false;
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public LocalDateTime ToDateTime() {
        return val.toLocalDateTime();
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public double ToDouble() {
        return 0;
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public int ToInt32() {
        return 0;
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public long ToInt64() {
        return 0;
    }

    /// <summary>
    /// yyyy-MM-dd HH:mm:ss
    /// </summary>
    /// <returns></returns>
    public String ToStr() {
        LocalDateTime localDateTime = ToDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter); // "1986-04-08 12:30:00"
        return formattedDateTime;
    }
}
