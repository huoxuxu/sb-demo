package com.hxx.hdblite.tools.TypeChange.Impls;

import com.hxx.hdblite.tools.TypeChange.IChangeBaseType;

import java.time.LocalDateTime;

public class BooleanChangeBaseType implements IChangeBaseType {
    private boolean val;

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    public BooleanChangeBaseType(boolean val) {
        this.val = val;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    public BooleanChangeBaseType(Boolean val) {
        this.val = val;
    }

    /// <summary>
    /// !=0 为true
    /// </summary>
    /// <returns></returns>
    public boolean ToBoolean() {
        return val;
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
        return val ? 1 : 0;
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public int ToInt32() {
        return val ? 1 : 0;
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public long ToInt64() {
        return val ? 1 : 0;
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public String ToStr() {
        return val + "";
    }
}
