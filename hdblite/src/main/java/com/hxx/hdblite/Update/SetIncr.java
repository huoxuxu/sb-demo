package com.hxx.hdblite.Update;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.SetReturnModel;

import java.util.ArrayList;
import java.util.List;

/// <summary>
/// A++
/// </summary>
public class SetIncr extends SetWrapper {
    /// <summary>
///
/// </summary>
/// <param name="field"></param>
    public SetIncr(String field) {
        super(field);

    }

    /// <summary>
///
/// </summary>
/// <param name="dbType"></param>
/// <returns></returns>
    public SetReturnModel Build(HXXDBType dbType) {
        String setStr = this.Field + "=" + this.Field + "+1";
        List<Object> setParas = new ArrayList<>();

        return new SetReturnModel(setStr, setParas);
    }
}
