package com.hxx.hdblite.Test;

import com.hxx.hdblite.*;
import com.hxx.hdblite.tools.db.OracleDB;
import com.hxx.hdblite.Models.ConnInfo.OracleConnInfo;
import com.hxx.hdblite.Models.DbTable;
import com.hxx.hdblite.Query.Query;
import com.hxx.hdblite.Test.Models.HT1Model;
import com.hxx.hdblite.tools.FastJsonHelper;
import com.hxx.hdblite.tools.log.XTrace;

import java.util.ArrayList;
import java.util.List;

public class OracleDBTest {
    private static final String ConnCfgName = "default";

    public static void main(String[] args) {
        XTrace.UseConsole();

        try {
            OracleConnInfo oci = new OracleConnInfo("10.9.15.11", 1521, "devdb");
            String userid = "";
            oci.setUser(userid);
            String pwd = "";
            oci.setPassword(pwd);

            //case1(oci);
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
    }

    static void case1(OracleConnInfo oci) {
        try (DAL dal = OracleDB.Connect(ConnCfgName, oci)) {
            CDUHelper cdu = new CDUHelper(dal, "HT1");
            Query query = new Query(dal, "HT1");

            {
                String sql = "select \"Id\" from HT1";
                DbTable dbTable = query.FindSQL(sql, new ArrayList<>());

                System.out.println("个数： " + dbTable.getCount());
            }

            DbTable dbt = query.Select().Skip(2).Take(10).ToDbTable();
            Object ls1 = dbt.ToList(HT1Model.class);
            System.out.println("ls1:  " + FastJsonHelper.ToJson(ls1));

            List<HT1Model> ls = query.Select().Take(2).ToList(HT1Model.class);
            System.out.println("个数： " + ls.size());
            System.out.println(FastJsonHelper.ToJson(ls));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
