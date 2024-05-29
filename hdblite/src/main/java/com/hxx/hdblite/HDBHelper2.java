package com.hxx.hdblite;

import com.hxx.hdblite.Models.DbColumn;
import com.hxx.hdblite.Models.DbRow;
import com.hxx.hdblite.Models.DbTable;
import com.hxx.hdblite.tools.log.XTrace;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 最基础的JDBC访问数据库帮助类
 * 根据查询SQL和参数化的值的集合 查询
 * 参数模型使用HDbParameter
 * jdbc参数化方式：select * from T1Meta where ID=? and Name=?  参数集合：[1,"hxx"]
 * 耗时过长会强制打出日志
 */
public class HDBHelper2 implements AutoCloseable{
    public static Boolean IS_DEBUG = true;//是否调试。调试会打出SQL
    private static final float MaxSQLSecond = 1.8f;//SQL执行最大耗时。单位秒
    private Connection _conn;

    /**
     * @param conn 数据库连接信息
     */
    public HDBHelper2(Connection conn) {
        this._conn = conn;
    }

    /**
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        if (this._conn != null) {
            //System.out.println("dal Closed!");
            this._conn.close();
        }
    }

    /**
     * 返回受影响的行数
     *
     * @param sql        select * from t where id=? and name=?
     * @param paraValArr [1,"hxx"]
     * @return 返回受影响的行数
     * @throws Exception
     */
    public int ExecuteNon(String sql, List<Object> paraValArr) throws Exception {
        Connection conn = this._conn;
        long startTime = System.currentTimeMillis();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //处理参数
            BindPara(stmt, paraValArr);

            //executeUpdate 的返回值是一个整数，指示受影响的行数（即更新计数）
            return stmt.executeUpdate();
        } finally {
            WLog(startTime, sql, paraValArr);
        }
    }

    /**
     * 返回首行首列的值
     *
     * @param sql        select * from t where id=? and name=?
     * @param paraValArr [1,"hxx"]
     * @return 返回首行首列的值
     * @throws Exception
     */
    public Object ExecuteScalar(String sql, List<Object> paraValArr) throws Exception {
        DbTable dbt = ExecuteDbTable(sql, paraValArr);
        if (dbt.getCount() == 0) return null;

        List<DbRow> rows = dbt.getRows();
        DbRow row = rows.get(0);

        if (row.getCount() == 0) return null;

        Object[] itemArr = row.getItemArray();
        return itemArr[0];
    }

    /**
     * 查询SQL并返回数据，
     * 注意：如果没有数据，返回模型，但Rows为空
     *
     * @param sql        select * from t where id=? and name=?
     * @param paraValArr [1,"hxx"]
     * @return DbTable
     * @throws Exception
     */
    public DbTable ExecuteDbTable(String sql, List<Object> paraValArr) throws Exception {
        Connection conn = this._conn;
        long startTime = System.currentTimeMillis();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //处理参数
            BindPara(stmt, paraValArr);

            //PreparedStatement pstmt = con.prepareStatement(sql) ;
            //执行对数据库存储过程的调用
            //CallableStatement cstmt = con.prepareCall("{CALL demoSp(? , ?)}") ;
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs == null) return new DbTable(null);

                List<DbColumn> dcols = new ArrayList<>();

                //获取返回的列信息
                ResultSetMetaData md = rs.getMetaData();
                int fieldCount = md.getColumnCount();

                for (int j = 1; j <= fieldCount; j++) {
                    String colName = md.getColumnName(j);
                    int colType = md.getColumnType(j);

                    DbColumn dcol = new DbColumn(colName);
                    dcol.setColIndex(j - 1);
                    dcol.setDataType(colType);

                    dcols.add(dcol);
                }

                DbTable dbt = new DbTable(dcols);

                List<Object[]> drows = new ArrayList<>();

                while (true) {
                    if (!rs.next()) {
                        dbt.setRows(drows);

                        break;
                    }

                    //获取数据
                    int colCount = dcols.size();
                    List<Object> data = new ArrayList<>();
                    for (int k = 0; k < colCount; k++) {
                        Object val = rs.getObject(k + 1);

                        data.add(val);
                    }

                    drows.add(data.toArray());
                }

                return dbt;
            }
        } finally {
            WLog(startTime, sql, paraValArr);
        }
    }

    //辅助--------------------------
    //将一些特殊的基本类型转为常用的基本类型
//    public static Object OPVal(Object val) throws Exception {
//        //常用类型：Int,Long,Double,bool,LocalDateTime,String
//        //byte=>int
//        //short=>int
//        //float=>double
//        //oracle.sql.TIMESTAMP=>Date
//        //LocalDate=>LocalDateTime
//        //
//        Class cls = val.getClass();
//        if(cls==String.class){
//            return ((String)val);
//        }
//        else if (cls == Byte.class) {
//            return ((Byte) val).intValue();
//        } else if (cls == Short.class) {
//            return ((Short) val).intValue();
//        } else if (cls == Float.class) {
//            return ((Float) val).doubleValue();
//        } else if (cls == BigDecimal.class) {
//            return ((BigDecimal) val).doubleValue();
//        }
//        else if (cls.getName().equals("oracle.sql.TIMESTAMP")) {
//            //oracle.sql.TIMESTAMP=>Date
//            return ((oracle.sql.TIMESTAMP) val).timestampValue();
////            Method m = cls.getMethod("timestampValue");//转为：java.sql.Timestamp
////            Object v = (Date) m.invoke(val);
////            return v;
//        }else if (cls == LocalDate.class){
//            return ((LocalDate) val).atStartOfDay();
//        }else if(cls.getName().equals("java.sql.Timestamp")){
//            //java.sql.Timestamp=>java.time.LocalDateTime
//            return ((java.sql.Timestamp) val).toLocalDateTime();
//        }
//
//        return val;
//    }

    private void WLog(long startTime, String sql, List<Object> paraValArr) {
        String paraValArrStr = StringUtils.join(paraValArr, ",");

        long endTime = System.currentTimeMillis();
        long hstime = endTime - startTime;
        //超长的耗时都需要打出来
        //调试模式下全部打出来
        if (IS_DEBUG) {
            String msg = String.format("[%sms]%s [%s]", hstime, sql, paraValArrStr);
            XTrace.WriteLine(msg);
        }

        if (hstime > MaxSQLSecond * 1000) {
            String msg = String.format("SQL耗时过长！[%s ms]%s [%s]", hstime, sql, paraValArrStr);
            XTrace.WriteLine(msg);
        }
    }


    //辅助

    /**
     * 绑定sql参数
     *
     * @param stmt
     * @param paraValArr
     * @throws Exception
     */
    private static void BindPara(PreparedStatement stmt, List<Object> paraValArr) throws Exception {
        if (paraValArr.isEmpty()) return;

        //处理参数
        Integer paraCou = paraValArr.size();

        //注意，
        // 从1开始，
        // 绑定顺序，从左至右
        for (int num = 1; num <= paraCou; num++) {
            Object val = paraValArr.get(num - 1);

            // 转换，将java.util中的Date转换称java.sql中的Date
            Class vcls = val.getClass();
            if (vcls == Date.class) {
                Date dval = (Date) val;
                val = new java.sql.Date(dval.getTime());
            }

            stmt.setObject(num, val);
        }
    }

//    //事务方法
//    static void HTest2() throws Exception {
//        //jdbc:mysql://hostname/databaseName
//        String connStr = "jdbc:mysql://127.0.0.1:3306/hxxtestdb?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
//        HXXDBType hxxdbtype = HXXDBType.MySQL;
//
//        DAL dal = new DAL(connStr, hxxdbtype, "root", "xx123", true);
//
//        DBTrans dbTrans = new DBTrans(dal);
//        try {
//            dbTrans.CreateTrans();
//
//            HDBHelper2 db = new HDBHelper2(dal);
//            //String sql = "select * from t1";
//            //ArrayList<HashMap<String, Object>> rs = hdb.ExecuteQuery(sql, null);
//            //Integer c = rs.size();
//
//            //Object firstcell = hdb.ExecuteScalar(sql, null);
//
//            {
//                String sql = "Insert into t1 select 1,'hxx1'";
//                int size = db.ExecuteNon(sql, null);
//
//                System.out.println(size);
//            }
//            {
//                String sql = "Insert into t1 select 11,'hxx11'";
//                int size = db.ExecuteNon(sql, null);
//
//                System.out.println(size);
//            }
//
//            //throw new Exception("1009");
//            dbTrans.Commit();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            dbTrans.Rollback();
//        } finally {
//            dal.Disposed();
//        }
//    }

}
