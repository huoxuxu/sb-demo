package demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbConsole.model.KV;
import com.hxx.sbcommon.common.basic.text.StringUtil;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
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
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class Demo1Test {

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

            // SB事务
            TransactionInterceptor transactionInterceptor = null;
            PlatformTransactionManager platformTransactionManager = null;
        }

        // fastJSON
        {
            JSONValidator jsonValidator = null;
            JSON.toJSON(null);
        }

        // 枚举
        JdbcType smallint = JdbcType.SMALLINT;
        {
            String str = "123456";
            {
                String str1 = StringUtil.cut(str, 19);
                System.out.println(str1);
            }
            {
                String str1 = StringUtil.cut(str, 10);
                System.out.println(str1);
            }
            {
                String str1 = StringUtil.cut(str, 0);
                System.out.println(str1);
            }
            {
                String str1 = StringUtil.cut(str, 2);
                System.out.println(str1);
            }
        }
        System.out.println("ok");
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

//    public static Object case2() {
//        Demo demo = new Demo();
//        {
//            demo.setAnothor("HK");
//        }
//        Object javaObject = demo;
//
//        if (javaObject == null) {
//            return null;
//        }
//
//        // Map
//        if (javaObject instanceof Map) {
//            Map<Object, Object> map = (Map<Object, Object>) javaObject;
//
//            int size = map.size();
//
//            Map innerMap;
//            if (map instanceof LinkedHashMap) {
//                innerMap = new LinkedHashMap(size);
//            } else if (map instanceof TreeMap) {
//                innerMap = new TreeMap();
//            } else {
//                innerMap = new HashMap(size);
//            }
//
//            JSONObject json = new JSONObject(innerMap);
//
//            for (Map.Entry<Object, Object> entry : map.entrySet()) {
//                Object key = entry.getKey();
//                String jsonKey = TypeUtils.castToString(key);
//                Object jsonValue = toJSON(entry.getValue(), config);
//                json.put(jsonKey, jsonValue);
//            }
//
//            return json;
//        }
//
//        // 集合
//        if (javaObject instanceof Collection) {
//            Collection<Object> collection = (Collection<Object>) javaObject;
//
//            JSONArray array = new JSONArray(collection.size());
//
//            for (Object item : collection) {
//                Object jsonValue = toJSON(item, config);
//                array.add(jsonValue);
//            }
//
//            return array;
//        }
//
//        Class<?> clazz = javaObject.getClass();
//        if (clazz.isEnum()) {
//            return ((Enum<?>) javaObject).name();
//        } else if (clazz.isArray()) {
//            int len = Array.getLength(javaObject);
//
//            JSONArray array = new JSONArray(len);
//
//            for (int i = 0; i < len; ++i) {
//                Object item = Array.get(javaObject, i);
//                Object jsonValue = toJSON(item);
//                array.add(jsonValue);
//            }
//
//            return array;
//        }
//
//        // 基本类型
//        if (ParserConfig.isPrimitive2(clazz)) {
//            return javaObject;
//        }
//
//        ObjectSerializer serializer = config.getObjectWriter(clazz);
//        if (serializer instanceof JavaBeanSerializer) {
//            JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer) serializer;
//
//            JSONObject json = new JSONObject();
//            try {
//                Map<String, Object> values = javaBeanSerializer.getFieldValuesMap(javaObject);
//                for (Map.Entry<String, Object> entry : values.entrySet()) {
//                    json.put(entry.getKey(), toJSON(entry.getValue(), config));
//                }
//            } catch (Exception e) {
//                throw new JSONException("toJSON error", e);
//            }
//            return json;
//        }
//
//        String text = JSON.toJSONString(javaObject, config);
//        return JSON.parse(text);
//    }

}
