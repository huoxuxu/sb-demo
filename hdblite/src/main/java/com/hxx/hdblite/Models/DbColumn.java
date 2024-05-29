package com.hxx.hdblite.Models;

public class DbColumn {
    /// <summary>
    /// 列索引
    /// </summary>
    public int ColIndex;
    /// <summary>列名称</summary>
    public String Name;
    /// <summary>列对应的Java类型</summary>
    public int DataType;

    public DbColumn(String name) {
        Name = name;
    }

    //get set ---------


    public int getColIndex() {
        return ColIndex;
    }

    public void setColIndex(int colIndex) {
        ColIndex = colIndex;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getDataType() {
        return DataType;
    }

    public void setDataType(int dataType) {
        DataType = dataType;
    }
}
