package com.hxx.hdblite.tools.db;

import com.hxx.hdblite.DAL;
import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.ConnInfo.SQLiteConnInfo;
import com.hxx.hdblite.Models.HConnInfo;

public class SQLiteDB {

    public static DAL Connect(String cfgConnName, SQLiteConnInfo sqliteConnInfo) throws Exception {
        String connStr = sqliteConnInfo.GetConnStr();
        HConnInfo hci = new HConnInfo(cfgConnName, connStr, HXXDBType.SQLite);
        hci.setPassword(sqliteConnInfo.getPassword());

        DAL.RegistryByConnInfo(hci);

        return DAL.Create(cfgConnName);
    }

    public static DAL Connect(String cfgConnName, String sqliteDbPath) throws Exception {
        SQLiteConnInfo sqliteConnInfo = new SQLiteConnInfo(sqliteDbPath);

        String connStr = sqliteConnInfo.GetConnStr();
        HConnInfo hci = new HConnInfo(cfgConnName, connStr, HXXDBType.SQLite);
        hci.setPassword(sqliteConnInfo.getPassword());

        DAL.RegistryByConnInfo(hci);

        return DAL.Create(cfgConnName);
    }

}
