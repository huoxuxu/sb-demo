package com.hxx.hdblite.tools.db;

import com.hxx.hdblite.DAL;
import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.ConnInfo.OracleConnInfo;
import com.hxx.hdblite.Models.HConnInfo;

public class OracleDB {

    public static DAL Connect(String connStr, String oracleUser, String oraclePwd, String cfgConnName) throws Exception {
        HConnInfo hci = new HConnInfo(cfgConnName, connStr, HXXDBType.Oracle);
        hci.setUser(oracleUser);
        hci.setPassword(oraclePwd);

        DAL.RegistryByConnInfo(hci);

        return DAL.Create(cfgConnName);
    }

    public static DAL Connect(String cfgConnName, OracleConnInfo oracleConnInfo) throws Exception {
        String connStr = oracleConnInfo.GetConnStr();
        HConnInfo hci = new HConnInfo(cfgConnName, connStr, HXXDBType.Oracle);
        hci.setUser(oracleConnInfo.getUser());
        hci.setPassword(oracleConnInfo.getPassword());

        DAL.RegistryByConnInfo(hci);

        return DAL.Create(cfgConnName);
    }


}
