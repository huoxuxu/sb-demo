package com.hxx.hdblite.Test;

import com.alibaba.fastjson.JSON;
import com.hxx.hdblite.CDUHelper;
import com.hxx.hdblite.DAL;
import com.hxx.hdblite.tools.db.MySQLDB;
import com.hxx.hdblite.DBTrans;
import com.hxx.hdblite.Demo.T1Meta;
import com.hxx.hdblite.Demo.T1Model;
import com.hxx.hdblite.HDBHelper2;
import com.hxx.hdblite.Models.DbTable;
import com.hxx.hdblite.Query.Query;
import com.hxx.hdblite.Query.Where;
import com.hxx.hdblite.tools.log.XTrace;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HXXTest {

    static String CONNSTR = "jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
    static String MySQLUSER = "root";
    static String MySQLPWD = "xx123";

    public static void main(String[] args) {
        XTrace.UseConsole();
        //String connCfgName="default";

        try {
            case0();
//            HConnInfo hci=new HConnInfo(connCfgName,CONNSTR,HXXDBType.MySQL);
//            hci.setUser(MySQLUSER);
//            hci.setPassword(MySQLPWD);
//
//            DAL.RegistryByConnInfo(hci);

            //case6();

        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        } finally {
            //dal.close();
        }
        System.out.print("ok");
    }

    private static void case0() throws Exception{
        String sql="select * from book where name= '1??2'";
        List<Object> paras=new ArrayList<>();
        try(DAL dal = MySQLDB.Connect(CONNSTR, MySQLUSER, MySQLPWD)) {
            HDBHelper2 db = new HDBHelper2(dal.getConn());
            DbTable dbTable = db.ExecuteDbTable(sql, paras);
            System.out.println(JSON.toJSON(dbTable));
        }
    }

    //生成5W条数据
    private static void case1() throws Exception {
        DAL dal = MySQLDB.Connect(CONNSTR, MySQLUSER, MySQLPWD);

        Query query = new Query(dal, "T1Meta");
        CDUHelper cduh = new CDUHelper(dal, "T1Meta");
        try {
            Date now = new Date();
            T1Meta h1 = new T1Meta();
            for (int i = 1; i < 50001; i++) {
                h1.setID(i);
                h1.setName("hx" + i);
                h1.setName2("hxx");
                h1.setScore(30.123456F);
                h1.setCreateDate(now);
                h1.setUpdateDate(now);

                Map<String, Object> updDic = h1.GetUpdateDic();
                cduh.InsertByDic(updDic);
            }
        } finally {
            dal.close();
        }
    }

    //测试更新
    private static void case2() throws Exception {
        DAL dal = MySQLDB.Connect(CONNSTR, MySQLUSER, MySQLPWD);

        Query query = new Query(dal, "T1Meta");
        CDUHelper cduh = new CDUHelper(dal, "T1Meta");
        try {
            List<T1Meta> hls = query.Select().Take(1).ToList(T1Meta.class);
            T1Meta h = hls.get(0);
            Where where = Where.Equal(T1Meta.innerCls.ID, h.getID());

            //普通更新
            {
                T1Meta h1 = new T1Meta();
                h1.setName("测试的");
                h1.setName2("测试的2");
                h1.setScore(22.123456789F);//会在最后一位上四舍五入！！！

                Map<String, Object> updDic = h1.GetUpdateDic();
                cduh.Update(updDic, where);
            }
            //Oracle将字符串置为null和空的情况
            {
                T1Meta h1 = new T1Meta();
                h1.setName2("");

                Map<String, Object> updDic = h1.GetUpdateDic();
                cduh.Update(updDic, where);
            }
        } finally {
            dal.close();
        }
    }

    //测试新增
    private static void case3() throws Exception {
        DAL dal = MySQLDB.Connect(CONNSTR, MySQLUSER, MySQLPWD);

        Query query = new Query(dal, "T1Meta");
        CDUHelper cduh = new CDUHelper(dal, "T1Meta");
        try {
            Date now = new Date();

            T1Meta h1 = new T1Meta();
            h1.setID(200000);
            h1.setName("hx");
            h1.setName2("hxx");
            h1.setScore(30.123456F);
            h1.setCreateDate(now);
            h1.setUpdateDate(now);

            Map<String, Object> updDic = h1.GetUpdateDic();
            cduh.InsertByDic(updDic);
        } finally {
            dal.close();
        }
    }

    //测试删除
    private static void case4() throws Exception {
        DAL dal = MySQLDB.Connect(CONNSTR, MySQLUSER, MySQLPWD);

        Query query = new Query(dal, "T1Meta");
        CDUHelper cduh = new CDUHelper(dal, "T1Meta");
        try {
            Where where = Where.Equal(T1Meta.innerCls.ID, 2);

            cduh.Delete(where);
        } finally {
            dal.close();
        }
    }

    //事务
    static void case5() throws Exception {
        DAL dal = MySQLDB.Connect(CONNSTR, MySQLUSER, MySQLPWD);

        Query query = new Query(dal, "T1Meta");
        CDUHelper cduh = new CDUHelper(dal, "T1Meta");

        try (Connection conn = dal.getConn()) {
            DBTrans t = new DBTrans(conn);
            try {
                Where where = Where.GreaterThan(T1Meta.innerCls.ID, 0);
                cduh.Delete(where);
                //查询
                List<T1Meta> ls = query.Select().ToList(T1Meta.class);
                //新增
                {
                    Date now = new Date();

                    T1Meta h1 = new T1Meta();
                    h1.setID(2);
                    h1.setName("hx2");
                    h1.setName2("hxx2");
                    h1.setScore(30.123456F);
                    h1.setCreateDate(now);
                    h1.setUpdateDate(now);

                    Map<String, Object> updDic = h1.GetUpdateDic();
                    cduh.InsertByDic(updDic);
                }
                //查询
                List<T1Model> ls1 = query.ToList(T1Model.class);

                t.Commit();
            } catch (Exception ex) {
                t.Rollback();
                throw ex;
            }

        } finally {
            dal.close();
        }
    }

    //查询
    static void case6() throws Exception {
        DAL dal = MySQLDB.Connect(CONNSTR, MySQLUSER, MySQLPWD);

        Query query = new Query(dal, "T1Meta");
        CDUHelper cduh = new CDUHelper(dal, "T1Meta");
        try {
            Where where = Where.Equal(T1Meta.innerCls.ID, 2);
            Where where2 = Where.LessThan(T1Meta.innerCls.ID, 0);
            //查询个数
            long cou = query.FindCount();
            long cou1 = query.FindCount(where);

            //带条件查询
            List<T1Model> ls1 = query.Select().Where(where).ToList(T1Model.class);

            //查询单条
            T1Model m = query.Find(where, T1Model.class);
            T1Model m1 = query.Find(where2, T1Model.class);//查询为null

            //分页查询
            List<T1Model> fyls = query.Select().Skip(0).Take(10).ToList(T1Model.class);
            List<T1Model> fyls2 = query.Select().Skip(1).Take(10).ToList(T1Model.class);

            //查询全部
            long st = System.currentTimeMillis();

            DbTable dbt = query.Select().ToDbTable();
            long et = System.currentTimeMillis();
            System.out.println("hs:  " + (et - st));

            st = System.currentTimeMillis();

            List<T1Model> ls = query.Select().ToList(T1Model.class);
            et = System.currentTimeMillis();
            System.out.println("hs:  " + (et - st));

            st = System.currentTimeMillis();

            List<com.hxx.hdblite.Demo.Beans.T1> lss = dbt.ToList(com.hxx.hdblite.Demo.Beans.T1.class);
            et = System.currentTimeMillis();
            System.out.println("hs:  " + (et - st));
        } finally {
            dal.close();
        }
    }

}
