package com.hxx.hdblite.Query;

import com.hxx.hdblite.tools.StringTools;

import java.util.ArrayList;
import java.util.List;

public class GroupByClause {
    private String ColumnName;//列名
    private String Having;//Having过滤

    /**
     * @param columnName id
     * @param having     sum(id)>1
     */
    public GroupByClause(String columnName, String having) {
        ColumnName = columnName;
        Having = having;
    }

    /**
     * @param groupBys
     * @return
     */
    public static String Build(List<GroupByClause> groupBys) {
        if (groupBys.isEmpty()) return "";

        List<String> ls = new ArrayList<>();

        for (GroupByClause groupBy : groupBys) {
            if (groupBy.Having == null || groupBy.Having.isEmpty()) {
                ls.add(groupBy.ColumnName);
            } else {
                ls.add(String.format("%s HAVING %s", groupBy.ColumnName, groupBy.Having));
            }
        }

        String lsStr = StringTools.Join(ls, ",");
        return String.format("GROUP BY %s", lsStr);
    }

}
