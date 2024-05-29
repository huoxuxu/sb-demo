package com.hxx.hdblite.Update;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.SetReturnModel;

import java.util.ArrayList;
import java.util.List;

/// <summary>
/// A +=-*/ 1
/// </summary>
public class SetOP extends SetWrapper {
    /// <summary>字段值</summary>
    private Object Val;

    /// <summary>操作符</summary>
    private String OP = "=";

    public SetOP(String field, String op, Object val) {
        super(field);

        this.OP = op;
        this.Val = val;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="dbType"></param>
    /// <returns></returns>
    public SetReturnModel Build(HXXDBType dbType) {
        Object val = this.Val;
        List<Object> setParas = new ArrayList<>();
        if (val == null) {
            String str = this.Field + this.OP + "NULL";
            return new SetReturnModel(str, setParas);
        }

        //枚举按数字处理
        //if (val.GetType().IsEnum) val = (int) val;

        String setStr = this.Field + this.OP + "?";//$ "{this.Field} {this.OP} {qz}{setParaName}";
        setParas.add(val);

        return new SetReturnModel(setStr, setParas);
    }
}

