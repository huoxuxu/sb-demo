package com.hxx.hdblite.Query.WhereImpl;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.WhereReturnModel;
import com.hxx.hdblite.Query.Where;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class WhereOpNoDbParam extends Where {
    //操作符，大于、小于、等于、like等
    private String operationAndVal;

    /**
     *
     * @param field
     * @param operationAndVal
     */
    public WhereOpNoDbParam(String field, String operationAndVal) {
        super(field);
        this.operationAndVal=operationAndVal;
    }

    @Override
    public WhereReturnModel Build(HXXDBType dbType) throws Exception {
        List<Object>dbParaLs=new ArrayList<>();

        String sql=String.format("%s %s",this.Field,this.operationAndVal);

        return new WhereReturnModel(sql,dbParaLs);
    }

    @Override
    public String toString() {
        return this.Field+" "+this.operationAndVal;
    }
}
