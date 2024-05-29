package com.hxx.hdblite.Query.WhereImpl;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.WhereReturnModel;
import com.hxx.hdblite.Query.Where;

import java.util.ArrayList;

/**
 *
 */
public class WhereNull extends Where {
    private boolean isNotNull;

    /**
     * @param field
     * @param isNotNull
     */
    public WhereNull(String field, boolean isNotNull) {
        super(field);
        this.isNotNull = isNotNull;
    }

    @Override
    public WhereReturnModel Build(HXXDBType dbType) {
        String sql = String.format("%s IS NULL", this.Field);
        if (this.isNotNull)
            sql = String.format("%s IS NOT NULL", this.Field);

        return new WhereReturnModel(sql, new ArrayList<>());
    }

    @Override
    public String toString() {
        String sql = " " + this.Field + " IS NULL ";
        if (this.isNotNull)
            sql = " " + this.Field + " IS NOT NULL ";
        return sql;
    }
}
