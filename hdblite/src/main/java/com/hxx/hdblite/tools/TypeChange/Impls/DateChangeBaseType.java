package com.hxx.hdblite.tools.TypeChange.Impls;

import com.hxx.hdblite.tools.TypeChange.IChangeBaseType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateChangeBaseType implements IChangeBaseType {
    private Date val;

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    public DateChangeBaseType(Date val) {
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
        Instant instant = val.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
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
        String formattedDateTime = ToDateTime().format(formatter); // "1986-04-08 12:30:00"
        return formattedDateTime;
    }
}
