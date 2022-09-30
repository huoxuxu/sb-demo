package demo;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.json.JsonUtil;
import models.KV;
import models.Order;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.LogFactory;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

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

            // 事务
            TransactionAspectSupport transactionAspectSupport = null;
            AbstractPlatformTransactionManager abstractPlatformTransactionManager = null;
            DataSourceTransactionManager dataSourceTransactionManager = null;
        }

        // 枚举
        JdbcType smallint = JdbcType.SMALLINT;
        System.out.println("");
    }

    public static void case1() {
        MetaClass metaClass = MetaClass.forClass(Order.class, new DefaultReflectorFactory());
        // 获取所有有get方法的属性名
        String[] getterNames = metaClass.getGetterNames();
        System.out.println(JsonUtil.toJSON(getterNames));
        // 是否有默认构造方法
        boolean hasDefaultConstructor = metaClass.hasDefaultConstructor();
        System.out.println("是否有默认的构造方法：" + hasDefaultConstructor);
        // 某个属性是否有get/set方法
        boolean hasGetter = metaClass.hasGetter("goodsName");
        System.out.println("goodsName属性是否有get方法：" + hasGetter);
        boolean hasSetter = metaClass.hasSetter("goodsName");
        System.out.println("goodsName属性是否有set方法：" + hasSetter);
        // 某个属性的类型
        Class getterType = metaClass.getGetterType("goodsName");
        System.out.println("goodsName属性类型：" + getterType.getName());
        // 通过Invoker对象调用get方法获取属性值
        try {
            Invoker invoker = metaClass.getGetInvoker("goodsName");
            Order testOrder = new Order("1", "001", "美的电压力锅");
            Object propertyValue = invoker.invoke(testOrder, null);
            System.out.println("goodsName属性值：" + propertyValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
