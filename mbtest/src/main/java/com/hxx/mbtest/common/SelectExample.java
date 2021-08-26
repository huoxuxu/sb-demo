package com.hxx.mbtest.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-07-01 9:54:01
 **/
@Data
public abstract class SelectExample {
    protected List<WhereCase> whereCases;
    protected String orderByCase;
    protected String groupByCase;

    protected Integer pageIndex;
    protected Integer pageSize;

    public SelectExample() {
        whereCases = new ArrayList<>();
    }

    protected void addWhereCase(WhereCase whereCase) {
        whereCases.add(whereCase);
    }
}
