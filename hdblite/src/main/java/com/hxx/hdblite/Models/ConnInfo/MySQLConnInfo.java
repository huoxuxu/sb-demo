package com.hxx.hdblite.Models.ConnInfo;

public class MySQLConnInfo {
    private String Host;
    private int Port;
    private String Dbname;
    private String User;
    private String Password;

    public MySQLConnInfo(String host, int port, String dbname) {
        Host = host;
        Port = port;
        Dbname = dbname;
    }

    /**
     * 获取MySQL 标准字符串
     *
     * @return string
     */
    public String GetConnStr() {
        //jdbc:mysql://127.0.0.1:3306/hxxtestdb?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://");
        sb.append(this.Host);
        sb.append(":");
        sb.append(this.Port);
        sb.append("/");
        sb.append(this.Dbname);
        sb.append("?");
        sb.append("useUnicode=true&characterEncoding=utf8&serverTimezone=GMT&useSSL=false");

        return sb.toString();
    }

    //get set-----------------------
    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


}
