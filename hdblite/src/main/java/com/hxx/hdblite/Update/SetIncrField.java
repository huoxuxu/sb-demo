package com.hxx.hdblite.Update;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.SetReturnModel;

import java.util.ArrayList;
import java.util.List;

/// <summary>
/// A+=B
/// </summary>
public class SetIncrField extends SetWrapper {
    private String FieldRight;

    /// <summary>
    ///
    /// </summary>
    /// <param name="field"></param>
    /// <param name="val"></param>
    public SetIncrField(String field, String fieldRight) {
        super(field);
        this.FieldRight = fieldRight;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="dbType"></param>
    /// <returns></returns>
    public SetReturnModel Build(HXXDBType dbType) {
        String setStr = this.Field + "=" + this.Field + "+" + this.FieldRight;
        List<Object> setParas = new ArrayList<>();

        return new SetReturnModel(setStr, setParas);
    }

}
