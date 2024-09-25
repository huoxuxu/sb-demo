package dal;

import com.hxx.sb.dal.common.DbConnUtil;
import com.hxx.sb.dal.common.DbSqlExecutor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DSqlTest {

    public static void main(String[] args) throws SQLException {

        //*----------------------添加数据-----------------------*/
        // 创建SqlExecutor对象控制器,并传入连接对象
        DbSqlExecutor sqlExecutor = new DbSqlExecutor(DbConnUtil.getConnection());
        // 定义sql语句,[插入数据]
        String sql = "insert into user_info(u_name,u_password,u_age,deposit,birth) values(?,?,?,?,?)";

        // 增加数据操作
        int i = sqlExecutor.executeUpdate(sql, "张三", "123456", 18, new BigDecimal(1000), LocalDateTime.now());

        //输出结果
        System.out.println("插入数据:" + i +"行");


        /*----------------------修改数据-----------------------*/
        sqlExecutor = new DbSqlExecutor(DbConnUtil.getConnection());
        // 定义sql语句,[修改数据]
        sql = "update user_info set u_password = ? where u_name = ?";

        // 修改数据操作
        i = sqlExecutor.executeUpdate(sql, "22222","张三");

        //输出结果
        System.out.println("修改数据:" + i +"行");




        /*----------------------删除数据-----------------------*/
        sqlExecutor = new DbSqlExecutor(DbConnUtil.getConnection());
        // 定义sql语句,[删除数据]
        sql = "delete from user_info where u_name = ?";

        // 删除数据操作
        i = sqlExecutor.executeUpdate(sql, "张三");

        //输出结果
        System.out.println("删除数据:" + i +"行");
    }

}
