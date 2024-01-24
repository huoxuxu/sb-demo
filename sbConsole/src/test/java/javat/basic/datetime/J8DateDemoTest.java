package javat.basic.datetime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbConsole.model.KV;
import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.basic.text.StringUtil;
import com.hxx.sbcommon.common.json.JsonUtil;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class J8DateDemoTest {

    private final static String datePattern = "yyyy-MM-dd HH:mm:ss";
    private final static String datePatternT = "yyyy-MM-dd'T'HH:mm:ss";
    private final static String datePatternTPlus = "yyyy-MM-dd'T'HH:mm:ss.SSS'+'SSSS";
    public static DateFormat dateFormat = new SimpleDateFormat(datePattern);
    public static DateFormat dateFormatT = new SimpleDateFormat(datePatternT);
    public static DateFormat dateFormatTPlus = new SimpleDateFormat(datePatternTPlus);

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);


    @Test
    public void test() {
        System.out.println("==============test==============");

        System.out.println("");
    }

    public static String dealDateFormat(String oldDate) {
        Date date1 = null;
        DateFormat df2 = null;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = df.parse(oldDate);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            date1 = df1.parse(date.toString());
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return df2.format(date1);
    }

    public static void main(String[] args) throws ParseException {
        {
            //获取默认的时区,输出为：Asia/Shanghai
            ZoneId zoneId = ZoneId.systemDefault();

            //定时时间格式，T表示时间元素的开始，
            String timePattern = "yyyy-MM-dd'T'HH:mm:ssZ";
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timePattern);

            //定义一个字符串类型的时间
            String time1 = "2023-06-06T00:18:14+0800";
            //将时间类型的字符串转化成ZonedDateTime
            ZonedDateTime zonedDateTime1 = ZonedDateTime.parse(time1, dateTimeFormatter);
            //获取东八区时间
            ZonedDateTime ZonedDateTime2 = zonedDateTime1.withZoneSameInstant(zoneId);

            //输出：2023-06-06T00:18:14+08:00[Asia/Shanghai]
            System.out.println(ZonedDateTime2);
        }
        {
            String str = "2016-08-9T10:01:54.123Z";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            //注意是空格+UTC
            String str1 = str.replace("Z", " UTC");
            Date d = format.parse(str1);
            System.out.println(d);
        }
        {
            String dateTimeStr = "2019-01-31T16:00:00.000+0000";
            String dateStr = dealDateFormat(dateTimeStr);
            System.out.println(dateStr);
        }
        {
            String dateTimeStr = "2019-01-31T16:00:00.000+0800";
            String dateStr = dealDateFormat(dateTimeStr);
            System.out.println(dateStr);
        }
        {
            String dateTimeStr = "2022-07-19T16:01:02.345+0000";
            Date date = dateFormatTPlus.parse(dateTimeStr);
            // 2022-07-19 16:01:02 忽略了时区！！！
            System.out.println(OftenUtil.DateTimeUtil.fmt2Str(date));
        }
        {
            String dateTimeStr = "2022-07-19T16:01:02.345+0800";
            Date date = dateFormatTPlus.parse(dateTimeStr);
            // 2022-07-19 16:01:02 忽略了时区！！！
            System.out.println(OftenUtil.DateTimeUtil.fmt2Str(date));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        {
            String dateTimeStr = "2022-07-19T16:01:02.345";
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
            System.out.println(dateTime);
        }
        {
            String dateTimeStr = "2022-07-19T16:01:02.345Z";
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeStr);
            LocalDateTime dateTime = zonedDateTime.toLocalDateTime();
            System.out.println(dateTime);
        }
        {
            String dateTimeStr = "2022-07-19T16:01:02.345+08:00";
            ZonedDateTime datetime = ZonedDateTime.parse(dateTimeStr);
            System.out.println(datetime);
        }
        DateTimeFormatter zonefmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        {
            String dateTimeStr = "2022-07-19T16:01:02.345Z";
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeStr, zonefmt);
            LocalDateTime dateTime = zonedDateTime.toLocalDateTime();
            System.out.println(dateTime);
        }
        {
            // 2018-12-26T20:28:33.213+05:30
            String dateTimeStr = "2022-07-19T16:01:02.345+00";
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeStr, zonefmt);
            LocalDateTime dateTime = zonedDateTime.toLocalDateTime();
            System.out.println(dateTime);
        }
        {
            // 2018-12-26T20:28:33.213+05:30[Asia/Calcutta]
            String dateTimeStr = "2022-07-19T16:01:02.345+00:00[Asia/Calcutta]";
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeStr);
            LocalDateTime dateTime = zonedDateTime.toLocalDateTime();
            System.out.println(dateTime);
        }
        System.out.println("==============\n");

        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String str = "2022-07-19T16:00:00.000+0000";
        LocalDateTime ldt = LocalDateTime.parse(str, fmt);
        System.out.println(ldt);

    }

}
