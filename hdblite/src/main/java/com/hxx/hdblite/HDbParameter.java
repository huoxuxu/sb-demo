package com.hxx.hdblite;

/**
 * SQL 参数模型
 */
public class HDbParameter {
    private String ParaName;
    private Object ParaVal;

    public HDbParameter(String paraName, Object paraVal) {
        ParaName = paraName;
        ParaVal = paraVal;
    }

    //get set------------


    public String getParaName() {
        return ParaName;
    }

    public void setParaName(String paraName) {
        ParaName = paraName;
    }

    public Object getParaVal() {
        return ParaVal;
    }

    public void setParaVal(Object paraVal) {
        ParaVal = paraVal;
    }
}
