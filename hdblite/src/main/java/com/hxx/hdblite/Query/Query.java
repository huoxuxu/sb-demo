package com.hxx.hdblite.Query;

import com.hxx.hdblite.DAL;
import com.hxx.hdblite.DALPool;
import com.hxx.hdblite.HDBHelper2;
import com.hxx.hdblite.HXXDBType;
import com.hxx.hdblite.Models.DbRow;
import com.hxx.hdblite.Models.DbTable;
import com.hxx.hdblite.Models.WhereReturnModel;
import com.hxx.hdblite.tools.StringTools;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ORM查询构造类
 */
public class Query implements AutoCloseable{
    private HXXDBType dbType;

    private HDBHelper2 DBHelper;

    private List<String> selects = new ArrayList<String>();
    private String tableName;
    private Where where;
    private List<OrderByClause> orderByArr = new ArrayList<>();
    private List<GroupByClause> groupByArr = new ArrayList<>();

    private int skip;
    private int take;

    /**
     * @param dal
     * @param tableName
     */
    public Query(DAL dal, String tableName) {
        this.dbType=dal.getDBType();
        this.tableName = tableName;
        this.DBHelper = new HDBHelper2(dal.getConn());
    }

    /**
     * @param dal
     * @param tableName
     */
    public Query(DALPool dal, String tableName) {
        this.dbType=dal.getDBType();
        this.tableName = tableName;
        this.DBHelper = new HDBHelper2(dal.getConn());
    }

    /**
     *
     * @throws SQLException
     */
    public void close() throws SQLException {
        if (this.DBHelper != null) {
            //System.out.println("dal Closed!");
            this.DBHelper.close();
        }
    }

    /**
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public DbTable ToDbTable() throws Exception {
        Object[] tup = parse();

        String sql = (String) tup[0];
        List<Object> whereParaArr = (List<Object>) tup[1];

        return this.DBHelper.ExecuteDbTable(sql, whereParaArr);
    }

    /**
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> ToList(Class cls) throws Exception {
        DbTable dbt = ToDbTable();

        return dbt.ToList(cls);
    }

    /**
     * 重置Query后查
     *
     * @param where
     * @return
     * @throws Exception
     */
    public <T> T Find(Where where, Class cls) throws Exception {
        List<T> ls = Select().Where(where).ToList(cls);
        if (ls.size() > 0) {
            return ls.get(0);
        }

        return null;
    }


    public long FindCount(Where where) throws Exception {
        DbTable dbt = Select("COUNT(1) COUNT")
                .Where(where)
                .ToDbTable();
        if (dbt.getCount() > 0) {
            DbRow firstRow = dbt.getRows().get(0);
            Object firstRC = firstRow.getItemArray()[0];
            return (long) (firstRC);
        }

        return 0;
    }

    /// <summary>
    /// 查询表行数
    /// </summary>
    /// <returns></returns>
    public long FindCount() throws Exception {
        DbTable dbt = Select("COUNT(1) COUNT").ToDbTable();
        if (dbt.getCount() > 0) {
            DbRow firstRow = dbt.getRows().get(0);
            Object firstRC = firstRow.getItemArray()[0];
            return (long) (firstRC);
        }

        return 0;
    }

    public long FindCount(String sql, List<Object> whereParaArr) throws Exception {
        DbTable dbt = this.FindSQL(sql, whereParaArr);
        if (dbt.getCount() > 0) {
            DbRow firstRow = dbt.getRows().get(0);
            Object firstRC = firstRow.getItemArray()[0];
            return (long) StringTools.ChangeType(firstRC, long.class);
        }

        return 0;
    }

    /// <summary>
    /// 按SQL查询
    /// </summary>
    /// <param name="sql">select * from t where id=:id</param>
    /// <param name="whereParaArr">参数名可以不带前缀</param>
    /// <returns></returns>
    public DbTable FindSQL(String sql, List<Object> whereParaArr) throws Exception {
        DbTable dbt = this.DBHelper.ExecuteDbTable(sql, whereParaArr);

        return dbt;
    }

    /**
     * [ID,Name]
     * 会重置Query对象
     *
     * @param colNames
     * @return
     */
    public Query Select(List<String> colNames) {
        this.selects = colNames;

        //重置Query
        {
            this.where = null;
            this.groupByArr = new ArrayList<GroupByClause>();
            this.orderByArr = new ArrayList<OrderByClause>();

            this.skip = 0;
            this.take = 0;
        }
        return this;
    }

    /**
     * Select *
     *
     * @return
     */
    public Query Select() {
        return Select(new ArrayList<String>());
    }

    /**
     * select id,name
     * 注意：此处指定的列名不是数据库返回的列名
     *
     * @param select select 子句，如id,name
     * @return
     */
    public Query Select(String select) {
        if (select == null
                || select.equalsIgnoreCase("*")
                || select.trim().isEmpty()) return Select();

        List<String> selects = StringTools.Split(select, ",", true);
        return Select(selects);
    }

    public Query Where(Where clause) throws Exception {
        if (this.where != null) throw new Exception("只能有一个Where子句");
        this.where = clause;

        return this;
    }

    public Query GroupBy(String field, String having) {
        GroupByClause groupByClause = new GroupByClause(field, having);
        this.groupByArr.add(groupByClause);

        return this;
    }

    /**
     * @param groupByClause groupby子句，如Date,Site Having count(1)>1
     * @return
     */
    public Query GroupByClause(String groupByClause) {
        if (StringTools.IsNullOrEmpty(groupByClause)) return this;

        List<String> arr = StringTools.Split(groupByClause, ",", true);

        for (String gby : arr) {
            GroupByClause odym = null;

            if (StringTools.ContainsIgnoreCase(gby, "having")) {
                List<String> arr1 = StringTools.Split(gby, " ", true);
                String field = arr1.get(0);
                int fieldLen = field.length();
                String having = StringTools.SubStr(gby, fieldLen);

                odym = new GroupByClause(field, having);
            } else
                odym = new GroupByClause(gby, "");

            this.groupByArr.add(odym);
        }

        return this;
    }

    public Query OrderBy(String field, boolean desc) {
        OrderByClause ody = new OrderByClause(field, desc);
        this.orderByArr.add(ody);

        return this;
    }

    public Query OrderByClause(String orderByClause) throws Exception {
        if (StringTools.IsNullOrEmpty(orderByClause)) return this;

        List<String> arr = StringTools.Split(orderByClause, ",", true);

        for (String ody : arr) {
            OrderByClause odym = null;
            List<String> arr1 = StringTools.Split(ody, " ", true);
            int len = arr1.size();
            if (len == 1) {
                String field = arr1.get(0);
                odym = new OrderByClause(field, false);
            } else if (len == 2) {
                String field = arr1.get(0);
                String descStr = arr1.get(1);
                boolean desc = StringTools.EqualsIgnoreCase(descStr, "desc");
                odym = new OrderByClause(field, desc);
            } else {
                throw new Exception("orderBy子句语法错误！");
            }

            this.orderByArr.add(odym);
        }

        return this;
    }

    public Query Skip(int skip) {
        this.skip = skip;

        return this;
    }

    public Query Take(int take) {
        this.take = take;

        return this;
    }

    //辅助========
    //解析当前类中字段，返回SQL和对应位置的参数
    private Object[] parse() throws Exception {
        String select = "*";
        String clause = "";
        List<Object> whereParaArr = new ArrayList<>();
        HXXDBType dbType = this.dbType;

        String orderBy = OrderByClause.Build(this.orderByArr);
        String groupBy = GroupByClause.Build(this.groupByArr);

        //处理select
        if (!this.selects.isEmpty()) {
            select = StringUtils.join(this.selects, " , ");
        }
        //处理Where
        if (this.where != null) {
            WhereReturnModel retm = this.where.Build(dbType);
            clause = "Where " + retm.getWhereSQL();
            whereParaArr = retm.getWhereValParas();
        }

        String tableName = this.tableName;
        //合并Select Where GroupBy OrderBy
        String sql = String.format("SELECT %s FROM %s %s %s %s", select, tableName, clause, groupBy, orderBy);

        // 分页，目前只支持MySQL和SQLite分页
        //根据数据库类型生成分页SQL
        if (this.skip > 0 || this.take > 0) {
            if (this.skip > 0 && this.take < 1) throw new Exception("如果指定了Skip，则必须Take！");

            if (dbType == HXXDBType.MySQL || dbType == HXXDBType.SQLite) {
                sql = String.format("%s LIMIT %s , %s", sql, this.skip, this.take);
            } else if (dbType == HXXDBType.Oracle) {
/*
Select * From (
Select T0____.*, rownum as rowNumber From (
Select ParentID,count(id) From ztgk.SiteKPI Where ParentID>3 Group By ParentID Order By ParentID desc
) T0____ Where rownum<=150
) T1____ Where rowNumber>50
*/
                StringBuilder sqlsb = new StringBuilder();
                sqlsb.append("Select * From ( ");
                sqlsb.append("Select T0____.*, rownum as rowNumber From ( ");
                String str = String.format("SELECT %s FROM %s %s %s %s ",
                        select, this.tableName,
                        clause, groupBy, orderBy);
                sqlsb.append(str);
                str = String.format(") T0____ Where rownum<=%s ", this.skip + this.take);
                sqlsb.append(str);
                str = String.format(") T1____ Where rowNumber>%s ", this.skip);
                sqlsb.append(str);

                sql = sqlsb.toString();
            } else {
                throw new Exception("暂不支持特殊数据库FenYe！");
            }
        }

        return new Object[]{sql, whereParaArr};
    }

}
