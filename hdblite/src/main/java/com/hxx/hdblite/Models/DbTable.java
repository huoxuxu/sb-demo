package com.hxx.hdblite.Models;

import java.util.ArrayList;
import java.util.List;

public class DbTable {
    private List<DbColumn> Cols = new ArrayList<>();
    private List<DbRow> Rows = new ArrayList<>();

    public DbTable(List<DbColumn> cols) {
        Cols = cols;
    }

    /**
     * 将DbTable转为指定类型
     *
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> ToList(Class cls) throws Exception {
        List<T> ls = new ArrayList<>();

        List<DbRow> rows = getRows();
        for (DbRow row : rows) {
            T t = row.ToEntity(cls);
            ls.add(t);
        }

        return ls;
    }

    //get set-------

    public List<DbColumn> getCols() {
        return Cols;
    }


    public List<DbRow> getRows() {
        return Rows;
    }

    public void setRows(List<Object[]> rows) {
        List<DbRow> rows1 = new ArrayList<>();
        for (Object[] rowArray : rows) {
            DbRow drow = new DbRow(this.Cols, rowArray);

            rows1.add(drow);
        }

        Rows = rows1;
    }

    public int getCount() {
        return this.Rows.size();
    }

}
