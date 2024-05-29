package com.hxx.hdblite.tools.TypeChange;

import java.time.LocalDateTime;

/**
 * 常用类型：Int,Long,Double,bool,LocalDateTime,String
 */
public interface IChangeBaseType {
    /// <summary></summary>
    /// <returns></returns>
    int ToInt32();

    /// <summary></summary>
    /// <returns></returns>
    long ToInt64();

    /// <summary></summary>
    /// <returns></returns>
    double ToDouble();

    /// <summary></summary>
    /// <returns></returns>
    boolean ToBoolean();

    /// <summary></summary>
    /// <returns></returns>
    LocalDateTime ToDateTime();

    /// <summary></summary>
    /// <returns></returns>
    String ToStr();
}
