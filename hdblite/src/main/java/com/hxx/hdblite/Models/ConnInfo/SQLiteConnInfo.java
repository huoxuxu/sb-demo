package com.hxx.hdblite.Models.ConnInfo;

public class SQLiteConnInfo {
    private String DbPath;
    private String Password;

    public SQLiteConnInfo(String DbPath) {
        this.DbPath = DbPath;
    }

    /**
     * 获取 数据库 标准字符串
     *
     * @return string
     */
    public String GetConnStr() {
        //jdbc:sqlite:sample.db
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:sqlite:");
        sb.append(this.DbPath);

        return sb.toString();
    }

    public String getDbPath() {
        return DbPath;
    }

    public void setDbPath(String dbPath) {
        DbPath = dbPath;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
