package com.hxx.hdblite.tools.TypeChange.Impls;

import com.hxx.hdblite.tools.TypeChange.IChangeBaseType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BigDecimalChangeBaseType implements IChangeBaseType {
    private BigDecimal val;

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    public BigDecimalChangeBaseType(BigDecimal val) {
        this.val = val;
    }

    /// <summary>
    /// !=0 为true
    /// </summary>
    /// <returns></returns>
    public boolean ToBoolean() {
        return val.doubleValue() != 0;
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
        return val.doubleValue();
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public int ToInt32() {
        return val.intValue();
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public long ToInt64() {
        return val.longValue();
    }

    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
    public String ToStr() {
        return val + "";
    }
}
