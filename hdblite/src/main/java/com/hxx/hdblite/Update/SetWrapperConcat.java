package com.hxx.hdblite.Update;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.SetReturnModel;
import com.hxx.hdblite.tools.StringTools;

import java.util.ArrayList;
import java.util.List;

public class SetWrapperConcat extends SetWrapper {
    private String joinStr;
    private List<SetWrapper> wrapperArr;

    /// <summary>
    ///
    /// </summary>
    /// <param name="joinStr"></param>
    /// <param name="wrapperArr"></param>
    public SetWrapperConcat(String joinStr, List<SetWrapper> wrapperArr) {
        super("");

        this.joinStr = joinStr;
        this.wrapperArr = wrapperArr;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="dbType"></param>
    /// <returns></returns>
    public SetReturnModel Build(HXXDBType dbType) {
        List<Object> setParas = new ArrayList<>();
        List<String> ls = new ArrayList<>();
        for (SetWrapper wrap : wrapperArr) {
            SetReturnModel sr = wrap.Build(dbType);
            ls.add(sr.getSetSQL());
            setParas.addAll(sr.getSetParas());
        }

        String sql = StringTools.Join(ls, " , ");
        return new SetReturnModel(sql, setParas);
    }

}
