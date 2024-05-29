package com.hxx.hdblite.Models.ConnInfo;

public class OracleConnInfo {
    private String Host;
    private int Port;
    private String ServiceName;
    private String User;
    private String Password;

    public OracleConnInfo(String host, int port, String serviceName) {
        Host = host;
        Port = port;
        ServiceName = serviceName;
    }

    /**
     * 获取MySQL 标准字符串
     *
     * @return string
     */
    public String GetConnStr() {
        //jdbc:oracle:thin:@//127.0.0.1:1521/orcl
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:oracle:thin:@//");
        sb.append(this.Host);
        sb.append(":");
        sb.append(this.Port);
        sb.append("/");
        sb.append(this.ServiceName);

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
