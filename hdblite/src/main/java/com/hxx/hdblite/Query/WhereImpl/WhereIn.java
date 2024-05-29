package com.hxx.hdblite.Query.WhereImpl;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.WhereReturnModel;
import com.hxx.hdblite.Query.Where;
import com.hxx.hdblite.tools.StringTools;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class WhereIn extends Where {
    /**
     * @var array [mixed]
     */
    private List<Object> val;
    /**
     * 操作符
     */
    private String operation = " IN ";

    /**
     * @param field
     * @param val
     */
    public WhereIn(String field, List<Object> val) {
        super(field);

        this.val = val;
    }

    @Override
    public WhereReturnModel Build(HXXDBType dbType) throws Exception {
        List<Object> paLs = new ArrayList<>();

        List<String> zqArr = new ArrayList<>();

        int len = this.val.size();
        for (int i = 0; i < len; i++) {
            Object paValItem = this.val.get(i);

            paLs.add(paValItem);
            zqArr.add("?");
        }


        String inparaStr = StringTools.Join(zqArr, ",");
        //ID IN (?,?,?)
        String sql = String.format("%s %s (%s)", this.Field, this.operation, inparaStr);

        return new WhereReturnModel(sql, paLs);
    }

    @Override
    public String toString() {
        return this.Field + " " + this.operation + " (Count:" + this.val.size() + ")";
    }
}
