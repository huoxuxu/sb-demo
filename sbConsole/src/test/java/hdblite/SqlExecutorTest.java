package hdblite;

import com.alibaba.fastjson.JSON;
import com.hxx.hdblite.*;
import com.hxx.hdblite.Models.DbTable;
import com.hxx.hdblite.tools.db.MySQLDB;
import com.hxx.sbConsole.SbConsoleApplication;
import lombok.var;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class SqlExecutorTest {

    static String CONNSTR = "jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";
    static String MySQLUSER = "root";
    static String MySQLPWD = "xx123";


    @Test
    public void test() {
        System.out.println("==============test==============");
        try {

            System.out.println("ok!");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        } finally {
            //dal.close();
        }
    }

    @Test
    public void case0() throws Exception {
        try {
            {
                String sql = "select * from book where name= '1?2\\'3\"4' and name= ?";
                List<Object> paras = new ArrayList<>();
                // 测试查询带特殊字符
                paras.add("1?2'3\"4");
                try (DAL dal = MySQLDB.Connect(CONNSTR, MySQLUSER, MySQLPWD)) {
                    HDBHelper2 db = new HDBHelper2(dal.getConn());
                    DbTable dbTable = db.ExecuteDbTable(sql, paras);
                    System.out.println(JSON.toJSON(dbTable));
                }
            }
            {
                String sql = "select * from book where name= \"1?2'3\\\"4\" and name= ?";
                List<Object> paras = new ArrayList<>();
                // 测试查询带特殊字符
                paras.add("1?2'3\"4");
                try (DAL dal = MySQLDB.Connect(CONNSTR, MySQLUSER, MySQLPWD)) {
                    HDBHelper2 db = new HDBHelper2(dal.getConn());
                    DbTable dbTable = db.ExecuteDbTable(sql, paras);
                    System.out.println(JSON.toJSON(dbTable));
                }
            }
        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }

    @Test
    public void case1() throws Exception {
        try {
            {
                String sql = "update book set name = ? where id=?";
                try (Connection connection = DriverManager.getConnection(CONNSTR, MySQLUSER, MySQLPWD)) {
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, "1\n2");
                        statement.setInt(2, 3);
                        statement.executeUpdate();
                    }
                }
            }
            {
                String sql = "insert into book (code,name) values (?,?)";
                try (Connection connection = DriverManager.getConnection(CONNSTR, MySQLUSER, MySQLPWD)) {
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, UUID.randomUUID() + "");
                        statement.setString(2, "1\n2\"3'4");
                        statement.executeUpdate();
                    }
                }
            }
            {
                var name = "1\n2\"3'4";
                String sql = "insert into book (code,name) values ('" + UUID.randomUUID() + "','" + name + "')";
                try (Connection connection = DriverManager.getConnection(CONNSTR, MySQLUSER, MySQLPWD)) {
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.executeUpdate();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }

    @Test
    public void case1_1() throws Exception {
        try {
            {
                var name = "1\n2\\\"3\\'4";
                String sql = "insert into book (code,name) values ('" + UUID.randomUUID() + "','" + name + "')";
                // insert into book (code,name) values ('851bd1db-cd4f-41b6-9d44-6dbd7ca045a9','1\n2\"3\'4')
                try (Connection connection = DriverManager.getConnection(CONNSTR, MySQLUSER, MySQLPWD)) {
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.executeUpdate();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }

    @Test
    public void case2() throws Exception {
        try {
            // IN 占位
            {
                String sql = "select * from book where name in (\"1?2'3\\\"45\", ?)";
                List<Object> paras = new ArrayList<>();
                // 测试查询带特殊字符
                paras.add("1?2'3\"4");
                try (DAL dal = MySQLDB.Connect(CONNSTR, MySQLUSER, MySQLPWD)) {
                    HDBHelper2 db = new HDBHelper2(dal.getConn());
                    DbTable dbTable = db.ExecuteDbTable(sql, paras);
                    System.out.println(JSON.toJSON(dbTable));
                }
            }
            var a = "\"1\"2\"";
            System.out.println(a.length());// 5
            var a1 = "\"1\\\"2\"";
            System.out.println(a1.length());// 6

        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }
}
