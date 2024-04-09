package com.hxx.sbcommon.common.other.pageSeparate;

/**
 * 分页工具类，传入SQL，生成对应的分页SQL语句
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-04-15 12:36:21
 **/
public abstract class PageSeparate {

    protected Pageable page;

    public PageSeparate(Pageable page) {
        this.page = page;
    }

    public abstract String getPageSql(String originSql);

    public static class MySQLPageSeparate extends PageSeparate {

        public MySQLPageSeparate(Pageable pageable) {
            super(pageable);
        }

        @Override
        public String getPageSql(String originSql) {
            return originSql +
                    " LIMIT ?, ? ";// skip, take
        }
    }

    public static class OracleDBPageSeparate extends PageSeparate {

        public OracleDBPageSeparate(Pageable pageable) {
            super(pageable);
        }

        @Override
        public String getPageSql(String originSql) {
            /*
            -- pageNum=1, pageSize=3
            -- Skip (pageNum-1)*pageSize, pageSize
            -- 1 0 3
            -- 2 3 3
            -- 3 6 3
            -- Oracle Rownum skip, skip+take
            -- 1 0 3
            -- 2 3 6
            -- 3 6 9
            SELECT * FROM (
                SELECT TMP_PAGE.*, ROWNUM TMP_PAGE_ROW_ID FROM (

                SELECT CATRGORY,count(1) c
                FROM demo230301 d
                GROUP BY CATRGORY

                ) TMP_PAGE WHERE ROWNUM <= 6
            ) WHERE TMP_PAGE_ROW_ID > 3
            */
            int leftVal = this.page.getSkip();
            int rightVal = leftVal + this.page.getTake();

            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT * FROM ( ");
            sqlBuilder.append("SELECT TMP_PAGE.*, ROWNUM TMP_PAGE_ROW_ID FROM ( ");
            // 原SQL 需要去除末尾分号
            sqlBuilder.append(originSql.trim());

            sqlBuilder.append(" ) TMP_PAGE WHERE ROWNUM <= ?");// rightVal
            sqlBuilder.append(" ) WHERE TMP_PAGE_ROW_ID > ?");// leftVal
            return sqlBuilder.toString();
        }
    }

    public static class SQLServer2012PageSeparate extends PageSeparate {

        public SQLServer2012PageSeparate(Pageable pageable) {
            super(pageable);
        }

        @Override
        public String getPageSql(String originSql) {
            /*
            -- pageNum=1, pageSize=3
            -- Skip
            -- 1 0 3
            -- 2 3 3
            -- 3 6 3
            -- SQLServer2012 OFFSET FETCH NEXT
            -- 1 0 3
            -- 2 3 3
            -- 这将从第11行开始返回5行结果集
            SELECT * FROM table_name ORDER BY column_name
            OFFSET 10 ROWS FETCH NEXT 5 ROWS ONLY;
            */
            return originSql +
                    " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";// skip, take
        }
    }

    public static class SQLServer2008PageSeparate extends PageSeparate {
        private final String orderByStr;

        public SQLServer2008PageSeparate(String orderByStr, Pageable pageable) {
            super(pageable);
            this.orderByStr = orderByStr;
        }

        @Override
        public String getPageSql(String originSql) {
            /*
            -- pageNum=1, pageSize=3
            -- Skip
            -- 1 0 3
            -- 2 3 3
            -- 3 6 3
            -- SQLServer08 Rownum
            -- 1 1 3
            -- 2 4 6
            -- 3 7 9
            -- 查询从第1行开始，第3行结束，包含第3行
            WITH CTE AS (
                SELECT *,
                    ROW_NUMBER() OVER (ORDER BY id,code) AS TMP_PAGE_ROW_ID
                FROM (
                    select * from demo
                ) TMP_PAGE
            )
            SELECT * FROM CTE
            WHERE TMP_PAGE_ROW_ID BETWEEN 1 AND 3
            */
            int leftVal = this.page.getSkip() + 1;
            int rightVal = this.page.getSkip() + this.page.getTake();

            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("WITH TMP_PAGE_CTE AS ( ");
            sqlBuilder.append("SELECT *,");
            sqlBuilder.append("ROW_NUMBER() OVER (ORDER BY ")
                    .append(this.orderByStr)
                    .append(") AS TMP_PAGE_ROW_ID ");
            sqlBuilder.append("FROM ( ");
            sqlBuilder.append(originSql);
            sqlBuilder.append(" ) TMP_PAGE ) SELECT * FROM TMP_PAGE_CTE ");
            sqlBuilder.append("WHERE TMP_PAGE_ROW_ID BETWEEN ? AND ?");// left,right
            return sqlBuilder.toString();
        }
    }
}
