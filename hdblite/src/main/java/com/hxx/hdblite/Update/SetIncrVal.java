package com.hxx.hdblite.Update;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.SetReturnModel;

import java.util.ArrayList;
import java.util.List;

/// <summary>
/// A+=30
/// </summary>
public class SetIncrVal extends SetWrapper {
    private Object Val;

    /// <summary>
    ///
    /// </summary>
    /// <param name="field"></param>
    /// <param name="val"></param>
    public SetIncrVal(String field, Object val) {
        super(field);

        this.Val = val;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="dbType"></param>
    /// <returns></returns>
    public SetReturnModel Build(HXXDBType dbType) {
        String setStr = this.Field + "=" + this.Field + "+" + this.Val;
        List<Object> setParas = new ArrayList<>();
        //setParas.add(this.Val);

        return new SetReturnModel(setStr, setParas);
    }

}
