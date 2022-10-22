package javat;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.basic.datetime.LocalDateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class LocalDateTest {

    @Test
    public void of() {
        {
            // 2014-05-21
            LocalDate localDate1 = LocalDate.of(2014, 5, 21);
            System.out.println(localDate1);

            // 2014-03-04
            LocalDate localDate2 = LocalDate.of(2014, Month.MARCH, 4);
            System.out.println(localDate2);
        }
        {
            // 2014-05-02
            LocalDate localDate1 = LocalDate.of(2014, Month.MAY, 2);
            System.out.println(localDate1);

            // 2015-05-02
            LocalDate localDate2 = localDate1.withYear(2015);
            System.out.println(localDate2);

            // 2014-07-02
            LocalDate localDate3 = localDate1.withYear(2014).withMonth(7);
            System.out.println(localDate3);
        }
    }

    // 更改 年，月，日，其他未修改的不变
    @Test
    public void withXXX() {
        {
            // 2014-05-02
            LocalDate localDate1 = LocalDate.of(2014, Month.MAY, 2);
            System.out.println(localDate1);

            // 2015-05-02
            LocalDate localDate2 = localDate1.withYear(2015);
            System.out.println(localDate2);

            // 2014-07-02
            LocalDate localDate3 = localDate1.withYear(2014).withMonth(7);
            System.out.println(localDate3);
        }
    }

    @Test
    public void parseXXX() {
        System.out.println("==============parseXXX==============");
        {
            LocalDate ldf1 = LocalDate.parse("20190102", DateTimeFormatter.BASIC_ISO_DATE);
            System.out.println("BASIC_ISO_DATE 字符串日期转换-指定格式：" + ldf1);
            LocalDate ldf2 = LocalDate.parse("2019-01-02", DateTimeFormatter.ISO_DATE);
            System.out.println("ISO_DATE 字符串日期转换-指定格式：" + ldf2);
            LocalDate ldf21 = LocalDate.parse("2019-01-02", DateTimeFormatter.ISO_LOCAL_DATE);
            System.out.println("ISO_LOCAL_DATE 字符串日期转换-指定格式：" + ldf21);

            // 注意：此处不能使用 YYYY 转换
            LocalDate ldf3 = LocalDate.parse("2019-01-02", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate ldf4 = LocalDate.parse("2019-12-02", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println("yyyy-MM-dd 字符串日期转换-指定格式：" + ldf3 + "  " + ldf4);
        }

    }

    @Test
    public void fromXXX(){
        LocalDateTime localDateTime=LocalDateTime.of(2021,8,21,11,23);

        // 2021-07-22
        LocalDate localDate = LocalDate.from(localDateTime);
        System.out.println(localDate);
    }

    @Test
    public void getXXX() {
        {
            LocalDate localDate = LocalDate.of(2014, 6, 21);
            int year = localDate.getYear();
            System.out.println(year);
            Month month = localDate.getMonth();
            System.out.println(month);

            int day = localDate.getDayOfMonth();
            System.out.println(day);
        }
    }

    @Test
    public void toXXX() {
        LocalDate localDate = LocalDate.of(2014, 6, 21);
        long days = localDate.toEpochDay();
        System.out.println(days);
    }

    @Test
    public void atXXX() {
        LocalDate localDate = LocalDate.of(2014, 6, 21);
        System.out.println(localDate);

        LocalDateTime localTime1 = localDate.atStartOfDay();
        System.out.println(localTime1);

        LocalDateTime localTime2 = localDate.atTime(16, 21);
        System.out.println(localTime2);

        LocalDate localDate2 = Year.of(2014).atMonth(6).atDay(21);
        System.out.println(localDate2);
    }

    @Test
    public void plusXXX() {
        LocalDate localDate = LocalDate.of(2014, 6, 21);
        LocalDate localDate1 = localDate.plusDays(5);
        System.out.println(localDate1);
        LocalDate localDate2 = localDate.plusMonths(3);
        System.out.println(localDate2);
        LocalDate localDate3 = localDate.plusWeeks(3);
        System.out.println(localDate3);
    }

    @Test
    public void minusXXX() {
        LocalDate localDate = LocalDate.of(2014, 6, 21);
        LocalDate localDate1 = localDate.minusMonths(5);
        System.out.println(localDate1);
        LocalDate localDate2 = localDate.minusWeeks(3);
        System.out.println(localDate2);
    }

    @Test
    public void PeriodXXX() {
        LocalDate startTime = LocalDateUtil.parse("2021-05-10");
        LocalDate endTime = LocalDateUtil.parse("2021-08-10");
        {
            Period d1 = Period.between(startTime, endTime);
            int y1 = d1.getYears();
            int m1 = d1.getMonths();
            int day1 = d1.getDays();
            System.out.println(MessageFormatter.arrayFormat("相差：{}年{}月{}日", new Object[]{y1, m1, day1}).getMessage());
        }

        {
            startTime = LocalDateUtil.parse("2021-05-09");
            Period d1 = Period.between(startTime, endTime);
            int y1 = d1.getYears();
            int m1 = d1.getMonths();
            int day1 = d1.getDays();

            System.out.println(MessageFormatter.arrayFormat("相差：{}年{}月{}日", new Object[]{y1, m1, day1}).getMessage());

        }
    }

}
