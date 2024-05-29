package com.hxx.hdblite.Models;

import com.hxx.hdblite.HXXDBType;

/**
 * 连接字符串信息
 */
public class HConnInfo {
    /**
     * 连接配置名
     */
    private String ConnCfgName;
    /**
     * 连接字符串
     */
    private String ConnStr;
    /**
     * 数据库类型
     */
    private HXXDBType DBType;

    //账号
    private String User;

    //密码
    private String Password;

    /**
     * @param connStr
     * @param DBType
     */
    public HConnInfo(String ConnCfgName, String connStr, HXXDBType DBType) {
        this.ConnCfgName = ConnCfgName;
        this.ConnStr = connStr;
        this.DBType = DBType;
    }

    //get set -----------------------

    public String getConnCfgName() {
        return ConnCfgName;
    }

    public String getConnStr() {
        return ConnStr;
    }

    public HXXDBType getDBType() {
        return DBType;
    }

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
