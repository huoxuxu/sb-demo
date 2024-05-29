package com.hxx.hdblite;

/**
 * 我的自定义数据库类型
 */
public enum HXXDBType {
    MySQL(0, "jdbc:mysql://localhost/EMP"),
    Oracle(1, "jdbc:oracle:thin:username/password@192.168.0.10:1521:EMP"),
    SQLServer(2, ""),
    SQLite(3, "");
    //org.apache.hive.jdbc.HiveDriver
    //Hive,

    HXXDBType(int Code, String ConnStrExample) {
        this.Code = Code;
        this.ConnStrExample = ConnStrExample;
    }

    private int Code;//
    private String ConnStrExample;//jdbc链接字符串的例子

    //get
    public int getCode() {
        return Code;
    }

    public String getConnStrExample() {
        return ConnStrExample;
    }
}
