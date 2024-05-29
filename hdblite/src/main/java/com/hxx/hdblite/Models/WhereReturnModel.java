package com.hxx.hdblite.Models;

import java.util.List;

/**
 * Where子类 Build返回的WhereSQL和参数化的集合
 */
public class WhereReturnModel {
    private String WhereSQL;
    private List<Object> WhereValParas;

    /**
     * @param whereSQL      Where ID=? AND Name=?
     * @param whereValParas [1,"2"]
     */
    public WhereReturnModel(String whereSQL, List<Object> whereValParas) {
        WhereSQL = whereSQL;
        WhereValParas = whereValParas;
    }

    //get set---------
    public String getWhereSQL() {
        return WhereSQL;
    }

    public void setWhereSQL(String whereSQL) {
        WhereSQL = whereSQL;
    }

    public List<Object> getWhereValParas() {
        return WhereValParas;
    }

    public void setWhereValParas(List<Object> whereValParas) {
        WhereValParas = whereValParas;
    }

}
