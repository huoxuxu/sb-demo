package demo;

import com.hxx.sbConsole.SbConsoleApplication;
import models.KV;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.type.JdbcType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class DemoTest {

    @Test
    public void test() {
        System.out.println("==============test==============");
        String tmpPath = System.getProperty("java.io.tmpdir");
        System.out.println("临时目录：" + tmpPath);

        // Mybatis 插件
        {
            Executor executor = null;

            StatementHandler statementHandler = null;
            RoutingStatementHandler routingStatementHandler = null;

            ParameterHandler parameterHandler = null;
            DefaultParameterHandler defaultParameterHandler = null;

            ResultSetHandler resultSetHandler = null;
            DefaultResultSetHandler defaultResultSetHandler = null;

            // 分页参数类
            RowBounds rowBounds = null;
            // sql+参数化
            BoundSql boundSql = null;

            // SqlSession
            SqlSession sqlSession = null;
            DefaultSqlSession defaultSqlSession = null;

            // 反射
            Reflector reflector = new Reflector(KV.class);
            String id = reflector.findPropertyName("id");
            String name = reflector.findPropertyName("Name");
        }

        {

        }

        // 枚举
        JdbcType smallint = JdbcType.SMALLINT;
        System.out.println("");
    }

}
