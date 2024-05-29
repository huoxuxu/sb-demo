package hdblite;

import com.alibaba.fastjson.JSON;
import com.hxx.hdblite.*;
import com.hxx.hdblite.Models.DbTable;
import com.hxx.hdblite.tools.db.MySQLDB;
import com.hxx.sbConsole.SbConsoleApplication;
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
            {
                String sql = "select * from book where name= \"1?2'3\"4\" and name= ?";
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

    public void case1() throws Exception {
        String sql = "INSERT INTO users (name, age) VALUES (?, ?)";
        String name = "hh";
        Integer age = 100;
        Connection connection = DriverManager.getConnection(CONNSTR, MySQLUSER, MySQLPWD);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setInt(2, age);
        statement.executeUpdate();
    }

}
