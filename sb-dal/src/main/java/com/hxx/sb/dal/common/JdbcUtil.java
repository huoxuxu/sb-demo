package com.hxx.sb.dal.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcUtil {


    public static void exec() throws Exception {
        String url = "";
        String userName = "";
        String userPwd = "";
        Connection connection = DriverManager.getConnection(url, userName, userPwd);

        String sql = "select * from demo where id in (?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setArray(1, connection.createArrayOf("Long", new Long[]{1L, 2L, 3L}));
        // exec
        ResultSet rs = statement.executeQuery();
        System.out.println(rs);
    }

}
