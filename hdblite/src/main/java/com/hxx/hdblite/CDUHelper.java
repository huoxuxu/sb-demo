package com.hxx.hdblite;

import com.hxx.hdblite.Models.SetReturnModel;
import com.hxx.hdblite.Models.WhereReturnModel;
import com.hxx.hdblite.Query.Where;
import com.hxx.hdblite.Update.SetWrapper;
import com.hxx.hdblite.tools.StringTools;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 增删改帮助类
 */
public class CDUHelper {
    private DAL dal;
    private String tableName;
    private HDBHelper2 DBHelper;

    /**
     * @param dal
     * @param tableName
     */
    public CDUHelper(DAL dal, String tableName) {
        this.dal = dal;
        this.tableName = tableName;
        this.DBHelper = new HDBHelper2(dal.getConn());
    }

    //增

    /**
     * 新增
     *
     * @param insertDic key就是需要拼接到SQL中的字段名
     * @return
     * @throws Exception
     */
    public int InsertByDic(Map<String, Object> insertDic) throws Exception {
        List<String> fieldLs = new ArrayList<String>();//ID,Name
        List<Object> fieldParaLs = new ArrayList<Object>();
        List<String> fieldParaPLs = new ArrayList<String>();//?,?

        for (Map.Entry<String, Object> entry : insertDic.entrySet()) {
            String field = entry.getKey();
            Object paraVal = entry.getValue();
            if (paraVal == null) continue;

            fieldLs.add(field);
            fieldParaLs.add(paraVal);
            fieldParaPLs.add("?");
        }

        String insFieldStr = StringTools.Join(fieldLs, ",");
        String insPFieldStr = StringTools.Join(fieldParaPLs, ",");

        //INSERT INTO T1Meta (ID,NAME) VALUES (1,'hxx')
        String sql = MessageFormat.format("INSERT INTO {0} ({1}) VALUES ({2})",
                this.tableName, insFieldStr, insPFieldStr);

        return this.DBHelper.ExecuteNon(sql, fieldParaLs);
    }

    //删
    public int Delete(Where where) throws Exception {
        HXXDBType DbType = this.dal.getDBType();
        WhereReturnModel ret = where.Build(DbType);
        String clause = ret.getWhereSQL();//where id>0
        List<Object> clausePara = ret.getWhereValParas();

        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1}",
                this.tableName, clause);
        return this.DBHelper.ExecuteNon(sql, clausePara);
    }

    //更新

    /**
     * @param updateDic key就是需要拼接到SQL中的字段名
     * @param where
     * @return
     * @throws Exception
     */
    public int Update(Map<String, Object> updateDic, Where where) throws Exception {
        HXXDBType DbType = this.dal.getDBType();

        WhereReturnModel ret = where.Build(DbType);
        String clause = ret.getWhereSQL();//where id>0
        List<Object> clausePara = ret.getWhereValParas();

        List<Object> setDBParaLs = new ArrayList<Object>();
        List<String> setLs = new ArrayList<String>();//id=?

        for (Map.Entry<String, Object> entry : updateDic.entrySet()) {
            String key = entry.getKey();
            Object paraVal = entry.getValue();

            setLs.add(MessageFormat.format("{0}=?", key));
            setDBParaLs.add(paraVal);
        }

        String setStr = StringTools.Join(setLs, ",");
        //Update t1 set name='',name2='' where id=0 and id1=0
        String sql = MessageFormat.format("UPDATE {0} SET {1} WHERE {2}",
                this.tableName, setStr, clause);

        setDBParaLs.addAll(clausePara);

        return this.DBHelper.ExecuteNon(sql, setDBParaLs);
    }

    /**
     * @param setWrapper
     * @param where
     * @return
     * @throws Exception
     */
    public int Update(SetWrapper setWrapper, Where where) throws Exception {
        HXXDBType DbType = this.dal.getDBType();

        WhereReturnModel ret = where.Build(DbType);
        String clause = ret.getWhereSQL();//where id>0
        List<Object> clausePara = ret.getWhereValParas();

        SetReturnModel setRet = setWrapper.Build(DbType);
        String setStr = setRet.getSetSQL();//set id=0
        List<Object> setPara = setRet.getSetParas();

        //Update t1 set name='',name2='' where id=0 and id1=0
        String sql = MessageFormat.format("UPDATE {0} SET {1} WHERE {2}",
                this.tableName, setStr, clause);
        setPara.addAll(clausePara);

        return this.DBHelper.ExecuteNon(sql, setPara);
    }
}
