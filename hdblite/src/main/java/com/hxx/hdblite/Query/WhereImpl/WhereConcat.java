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
public class WhereConcat extends Where {
    private String join;
    private List<Where> clauses;

    /**
     * @param join
     * @param clauses
     */
    public WhereConcat(String join, List<Where> clauses) {
        super("");
        this.join = join;
        this.clauses = clauses;
    }

    @Override
    public WhereReturnModel Build(HXXDBType dbType) throws Exception {
        List<String> whereStrls = new ArrayList<>();
        List<Object> whereParals = new ArrayList<>();

        for (Where clause : this.clauses) {
            WhereReturnModel wrm = clause.Build(dbType);

            whereStrls.add(wrm.getWhereSQL());
            whereParals.addAll(wrm.getWhereValParas());
        }

        String sql = StringTools.Join(whereStrls, this.join);

        return new WhereReturnModel(sql, whereParals);
    }

}
