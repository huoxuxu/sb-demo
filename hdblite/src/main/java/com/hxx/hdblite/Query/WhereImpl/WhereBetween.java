package com.hxx.hdblite.Query.WhereImpl;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.WhereReturnModel;
import com.hxx.hdblite.Query.Where;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class WhereBetween extends Where {
    private Object leftVal;
    private Object rightVal;
    //操作符
    private String operation = " BETWEEN ";

    /**
     * @param field
     * @param leftVal
     * @param rightVal
     */
    public WhereBetween(String field, Object leftVal, Object rightVal) {
        super(field);
        this.leftVal = leftVal;
        this.rightVal = rightVal;
    }

    @Override
    public WhereReturnModel Build(HXXDBType dbType) throws Exception {
        List<Object> paras = new ArrayList<>();
        paras.add(this.leftVal);
        paras.add(this.rightVal);

        //ID Between 1 AND 2
        String sql = String.format(" %s %s ? AND ? ", this.Field, this.operation);
        return new WhereReturnModel(sql, paras);
    }

    @Override
    public String toString() {
        return this.Field + " " + this.operation + " " + this.leftVal + " AND " + this.rightVal;
    }
}
