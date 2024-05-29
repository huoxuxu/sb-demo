package com.hxx.hdblite.Query.WhereImpl;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.WhereReturnModel;
import com.hxx.hdblite.Query.Where;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class WhereOp extends Where {
    private Object val;
    //操作符，大于、小于、等于、like等
    private String operation;

    /**
     * @param field
     * @param val
     * @param operation
     */
    public WhereOp(String field, Object val, String operation) {
        super(field);
        this.val = val;
        this.operation = operation;
    }

    @Override
    public WhereReturnModel Build(HXXDBType dbType) throws Exception {
        List<Object> dbParaLs = new ArrayList<>();
        dbParaLs.add(this.val);

        String sql = String.format("%s %s ?", this.Field, this.operation);

        return new WhereReturnModel(sql, dbParaLs);
    }

    @Override
    public String toString() {
        return this.Field + " " + this.operation + " " + this.val;
    }
}
