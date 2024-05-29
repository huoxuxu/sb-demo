package com.hxx.hdblite.tools.db;

import com.hxx.hdblite.DAL;
import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.ConnInfo.MySQLConnInfo;
import com.hxx.hdblite.Models.HConnInfo;

public class MySQLDB {

    public static DAL Connect(String connStr, String mySQLUser, String mySQLPwd) throws Exception {
        return Connect(connStr, mySQLUser, mySQLPwd, "default");
    }

    public static DAL Connect(String connStr, String mySQLUser, String mySQLPwd, String cfgConnName) throws Exception {
        HConnInfo hci = new HConnInfo(cfgConnName, connStr, HXXDBType.MySQL);
        hci.setUser(mySQLUser);
        hci.setPassword(mySQLPwd);

        DAL.RegistryByConnInfo(hci);

        return DAL.Create(cfgConnName);
    }

    public static DAL Connect(String cfgConnName, MySQLConnInfo mySQLConnInfo) throws Exception {
        String connStr = mySQLConnInfo.GetConnStr();
        HConnInfo hci = new HConnInfo(cfgConnName, connStr, HXXDBType.MySQL);
        hci.setUser(mySQLConnInfo.getUser());
        hci.setPassword(mySQLConnInfo.getPassword());

        DAL.RegistryByConnInfo(hci);

        return DAL.Create(cfgConnName);
    }


}
