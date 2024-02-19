package javat.io;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbConsole.model.KV;
import com.hxx.sbcommon.common.basic.text.BraceParser;
import com.hxx.sbcommon.common.basic.text.StringUtil;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbcommon.common.usual.TryRunUtil;
import lombok.var;
import models.Order;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.type.JdbcType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@var
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class BraceParserTest {

    @Test
    public void test() {
        System.out.println("==============test==============");
        {
            String[] arr = {
                    "123{}456{{1}}",
                    "1{}2{}3{}4{}56",
                    "1{}2{}3{}4{}5{}6{}",
                    "12{{",
                    "{{12",
                    "12}}",
                    "}}12",
                    "1{{}}2",
            };
            for (String item : arr) {
                TryRunUtil.run(d -> System.out.println(BraceParser.parse(item, "+", "-", "*", "/")));
                System.out.println("===========");
            }
        }
        {
            String[] arr = {
                    "",
                    " \r\n   \r\n ",
                    "123{}456",
            };
            for (String item : arr) {
                TryRunUtil.run(d -> System.out.println(BraceParser.parse(item)));
                System.out.println("===========");
            }
        }
        {
            String[] arr = {
                    "12{",
                    "{12"
            };
            for (String item : arr) {
                TryRunUtil.run(d -> System.out.println(BraceParser.parse(item)));
                System.out.println("===========");
            }
        }

        System.out.println("ok!");
    }

}
