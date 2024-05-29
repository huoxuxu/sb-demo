package com.hxx.hdblite.tools.TypeChange.Impls;

import com.hxx.hdblite.tools.TypeChange.IChangeBaseType;

import java.time.LocalDateTime;

public class Int16ChangeBaseType implements IChangeBaseType {
    private short val;

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    public Int16ChangeBaseType(short val) {
        this.val = val;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    public Int16ChangeBaseType(Short val) {
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
    ///
    /// </summary>
    /// <returns></returns>
    public LocalDateTime ToDateTime() {
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
        return val;
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
