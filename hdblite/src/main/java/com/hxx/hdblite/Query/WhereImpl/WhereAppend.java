package com.hxx.hdblite.Query.WhereImpl;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.WhereReturnModel;
import com.hxx.hdblite.Query.Where;

import java.util.ArrayList;

/**
 *
 */
public class WhereAppend extends Where {
    private String appendTxt;

    /**
     * @param appendTxt
     */
    public WhereAppend(String appendTxt) {
        super("");

        this.appendTxt = appendTxt;
    }

    @Override
    public WhereReturnModel Build(HXXDBType dbType) {
        String sql = this.appendTxt;
        return new WhereReturnModel(sql, new ArrayList<>());
    }

    @Override
    public String toString() {
        return this.appendTxt;
    }

}
