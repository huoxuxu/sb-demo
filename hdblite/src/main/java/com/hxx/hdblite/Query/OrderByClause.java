package com.hxx.hdblite.Query;

import com.hxx.hdblite.tools.StringTools;

import java.util.ArrayList;
import java.util.List;

public class OrderByClause {
    private String ColumnName;
    private boolean Desending;

    /**
     * @param columnName
     * @param desending
     */
    public OrderByClause(String columnName, boolean desending) {
        ColumnName = columnName;
        Desending = desending;
    }

    //实例

    /**
     * 返回不带OrderBy的排序子句
     *
     * @return
     */
    public String BuildImpl() {
        String asc = this.Desending ? "desc" : "asc";

        return String.format("%s %s", this.ColumnName, asc);
    }

    //Static

    /**
     * 返回带OrderBy的子句
     *
     * @param orderBys
     * @return
     */
    public static String Build(List<OrderByClause> orderBys) {
        if (orderBys.isEmpty()) return "";

        List<String> ls = new ArrayList<>();
        for (OrderByClause orderBy : orderBys) {
            String obstr = orderBy.BuildImpl();
            ls.add(obstr);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ORDER BY ");
        sb.append(StringTools.Join(ls, ","));

        return sb.toString();
    }

    //get set--------------


}
