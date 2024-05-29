package com.hxx.hdblite.tools.TypeChange.Impls;

import com.hxx.hdblite.tools.TypeChange.IChangeBaseType;

import java.time.LocalDateTime;

public class Int64ChangeBaseType implements IChangeBaseType {
    private long val;

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    public Int64ChangeBaseType(long val) {
        this.val = val;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    public Int64ChangeBaseType(Long val) {
        this.val = val;
    }

    /// <summary>
    /// !=0 为true
    /// </summary>
    /// <returns></returns>
    public boolean ToBoolean() {
        return val != 0;
    }

    /// <summary>
    /// 作为毫秒传入
    /// </summary>
    /// <returns></returns>
    public LocalDateTime ToDateTime() {
//        var y = new DateTime(1970, 1, 1);
//        var y1 = y.AddMilliseconds(val);
//        var ts = TimeZoneInfo.Local.GetUtcOffset(y1);
//        return y1.Add(ts);
        return LocalDateTime.MIN;
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public double ToDouble() {
        return val;
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public int ToInt32() {
        return (int) val;
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public long ToInt64() {
        return val;
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public String ToStr() {
        return val + "";
    }
}
