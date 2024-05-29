package com.hxx.hdblite.Models;

import java.util.List;

/**
 * Set子类 Build返回的SetSQL和参数化的集合
 */
public class SetReturnModel {
    private String setSQL;
    private List<Object> setParas;

    /**
     * @param setSQL   ID=? , Name=?
     * @param setParas [1,"2"]
     */
    public SetReturnModel(String setSQL, List<Object> setParas) {
        this.setSQL = setSQL;
        this.setParas = setParas;
    }

    //get&set

    public String getSetSQL() {
        return setSQL;
    }

    public void setSetSQL(String setSQL) {
        this.setSQL = setSQL;
    }

    public List<Object> getSetParas() {
        return setParas;
    }

    public void setSetParas(List<Object> setParas) {
        this.setParas = setParas;
    }

}
