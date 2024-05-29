package com.hxx.hdblite.Update;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.SetReturnModel;

import java.util.List;

/// <summary>
/// 处理Set的特殊情况
/// Set Num++
/// Set Num+=50
/// </summary>
public abstract class SetWrapper {
    /// <summary>字段名</summary>
    protected String Field;

    /// <summary>
    ///
    /// </summary>
    /// <param name="field"></param>
    public SetWrapper(String field) {
        this.Field = field;
    }

    public abstract SetReturnModel Build(HXXDBType dbType);

    /// <summary>
    /// 合并多个SetWrapper
    /// </summary>
    /// <param name="wrappers"></param>
    /// <returns></returns>
    public static SetWrapper Merge(List<SetWrapper> wrappers) {
        return new SetWrapperConcat(" , ", wrappers);
    }

    /// <summary>
    /// A=1
    /// </summary>
    /// <param name="field"></param>
    /// <param name="val"></param>
    /// <returns></returns>
    public static SetWrapper EQ(String field, Object val) {
        return new SetOP(field, "=", val);
    }

    //A++
    public static SetWrapper Incr(String field) {
        return new SetIncr(field);
    }

    //A+=B
    public static SetWrapper Incr(String field, String fieldRight) {
        return new SetIncrField(field, fieldRight);
    }

    //A+=30
    public static SetWrapper Incr(String field, Object val) {
        return new SetIncrVal(field, val);
    }


}
