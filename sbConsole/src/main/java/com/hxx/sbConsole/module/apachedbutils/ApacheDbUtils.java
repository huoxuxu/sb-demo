package com.hxx.sbConsole.module.apachedbutils;

import com.hxx.sbConsole.model.Employee;
import lombok.var;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@var
public class ApacheDbUtils {

    public static void demo() {

    }

    static void case1() throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        QueryRunner qr = queryRunner;
        // 查询单条记录
        {
            var id = 1;
            BeanHandler<Employee> handler = new BeanHandler<>(Employee.class);
            Employee emp = queryRunner.query("SELECT * FROM Employee WHERE id = ?", handler, id);
        }
        // 查询多条
        {
            List<Employee> users = queryRunner.query("SELECT * FROM users", new BeanListHandler<>(Employee.class));
        }
        // 更新
        {
            int rowsInserted = queryRunner.update("INSERT INTO users(name, email) VALUES(?, ?)", "John Doe", "john@example.com");
        }
        // 批量更新
        {
            Object[][] batchArgs = {
                    {"John", "john@example.com"},
                    {"Jane", "jane@example.com"}
            };
            queryRunner.batch("INSERT INTO users(name, email) VALUES(?, ?)", batchArgs);
        }
        // Count
        {
            Long count = queryRunner.query("SELECT COUNT(*) FROM users", new ScalarHandler<Long>(0));
        }
        // 批量
        {
            //        传入一个二维数组，高维是需要执行的sql语句的次数，低维为需要给sql语句中？赋的值
            Object[][] params = new Object[10][];
            for (int i = 0; i < params.length; i++) {
                params[i] = new Object[]{"xiaoming", "123456", "xiaoming@163.com", new Date()};
            }
            queryRunner.batch("insert into user(username,password,email,birthday) values(?,?,?,?)", params);
        }
        // 使用连接池
        {
//            DataSource dataSource = ...; // 初始化数据源
//            QueryRunner queryRunner = new QueryRunner(dataSource);
//            // 使用 queryRunner 执行操作，不需要手动关闭连接
        }
        // 事务
        {
//            Connection conn = ...; // 获取数据库连接
//            try {
//                conn.setAutoCommit(false); // 开始事务
//                // 执行一系列数据库操作
//                conn.commit(); // 提交事务
//            } catch (SQLException e) {
//                conn.rollback(); // 回滚事务
//                throw e;
//            } finally {
//                conn.setAutoCommit(true); // 确保设置自动提交
//                DbUtils.closeQuietly(conn); // 关闭连接
//            }
        }
        {
            String sql = "select * from users";
            Object result[] = (Object[]) qr.query(sql, new ArrayHandler());
            System.out.println(Arrays.asList(result));  //list  toString()
        }
        {
            String sql = "select count(*) from users";  //[13]  list[13]
            int count = ((Long) qr.query(sql, new ScalarHandler(1))).intValue();
            System.out.println(count);
        }
        {
            String sql = "select * from users";
            List<Map> list = (List) qr.query(sql, new MapListHandler());
            for (Map<String, Object> map : list) {
                for (Map.Entry<String, Object> me : map.entrySet()) {
                    System.out.println(me.getKey() + "=" + me.getValue());
                }
            }
        }
        {
            String sql = "select * from users";

            Map<String, Object> map = (Map) qr.query(sql, new MapHandler());
            for (Map.Entry<String, Object> me : map.entrySet()) {
                System.out.println(me.getKey() + "=" + me.getValue());
            }
        }
        {
            String sql = "select * from users";

            Map<Integer, Map> map = (Map) qr.query(sql, new KeyedHandler("id"));
            for (Map.Entry<Integer, Map> me : map.entrySet()) {
                int id = me.getKey();
                Map<String, Object> innermap = me.getValue();
                for (Map.Entry<String, Object> innerme : innermap.entrySet()) {
                    String columnName = innerme.getKey();
                    Object value = innerme.getValue();
                    System.out.println(columnName + "=" + value);
                }
                System.out.println("----------------");
            }
        }
        {
            String sql = "select * from users";
            List list = (List) qr.query(sql, new ColumnListHandler("id"));
            System.out.println(list);
        }
        {
            String sql = "select * from users";
            List<Object[]> list = (List) qr.query(sql, new ArrayListHandler());
            for (Object[] o : list) {
                System.out.println(Arrays.asList(o));
            }
        }
        {
            String sql = "select * from users";
            Object result[] = (Object[]) qr.query(sql, new ArrayHandler());
            System.out.println(Arrays.asList(result));  //list  toString()
        }
    }
}
