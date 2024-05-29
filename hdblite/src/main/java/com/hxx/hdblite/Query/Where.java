package com.hxx.hdblite.Query;

import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.WhereReturnModel;
import com.hxx.hdblite.Query.WhereImpl.*;

import java.util.List;

/**
 *
 */
public abstract class Where {
    protected String Field;

    /**
     * @param field
     */
    public Where(String field) {
        Field = field;
    }

    /**
     * 将Where转SQL
     *
     * @param dbType
     * @return
     */
    public abstract WhereReturnModel Build(HXXDBType dbType) throws Exception;

    //Static
    public static Where OrOP(List<Where> wheres) {
        return new WhereConcat(" OR ", wheres);
    }

    public static Where AndOP(List<Where> wheres) {
        return new WhereConcat(" AND ", wheres);
    }

    public static Where IsNull(String field) {
        return new WhereNull(field, false);
    }

    public static Where IsNotNull(String field) {
        return new WhereNull(field, true);
    }

    public static Where Between(String field, Object leftVal, Object rightVal) {
        return new WhereBetween(field, leftVal, rightVal);
    }

    public static Where In(String field, List<Object> vals) {
        return new WhereIn(field, vals);
    }

    public static Where Like(String field, Object val) {
        return new WhereOp(field, val, "LIKE");
    }

    public static Where Equal(String field, Object val) {
        return new WhereOp(field, val, "=");
    }

    public static Where NotEqual(String field, Object val) {
        return new WhereOp(field, val, "<>");
    }

    public static Where GreaterThan(String field, Object val) {
        return new WhereOp(field, val, ">");
    }

    public static Where GreaterOrEqual(String field, Object val) {
        return new WhereOp(field, val, ">=");
    }

    public static Where LessThan(String field, Object val) {
        return new WhereOp(field, val, "<");
    }

    public static Where LessOrEqual(String field, Object val) {
        return new WhereOp(field, val, "<=");
    }

    public static Where OP(String field,String operationAndVal){
        return new WhereOpNoDbParam(field,operationAndVal);
    }

}
