package com.hxx.hdblite.tools.TypeChange.Impls;

import com.hxx.hdblite.tools.TypeChange.IChangeBaseType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateChangeBaseType implements IChangeBaseType {
    private LocalDate val;

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    public LocalDateChangeBaseType(LocalDate val) {
        this.val = val;
    }

    /// <summary>
    ///
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
        return val.atStartOfDay();
    }

    /// <summary>
    /// utc 毫秒
    /// </summary>
    /// <returns></returns>
    public double ToDouble() {
        return 0;
    }

    /// <summary>
    /// utc 秒
    /// </summary>
    /// <returns></returns>
    public int ToInt32() {
        return 0;
    }

    /// <summary>
    /// utc 毫秒
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = val.format(formatter); // "1986-04-08 12:30:00"
        return formattedDateTime;
    }
}
