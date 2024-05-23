package com.hxx.sbConsole.module.apachedbutils;

import com.hxx.sbConsole.model.Employee;
import lombok.var;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@var
public class ApacheDbUtils {

    public static void demo() {

    }

    static void case1() throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
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
    }
}
