package javat.basic.datetime;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.basic.datetime.LocalDateTimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class DateTest {

    @Test
    public void of() {
        Date now = new Date();
        Long utc = now.getTime();//1639556697045  UTC时间 13位 毫秒
        System.out.println(utc);

    }

    @Test
    public void days() {
        // >=start <=end
        LocalDateTime start = LocalDateTimeUtil.parse("2022-06-29 00:00:00");
        LocalDateTime end = LocalDateTimeUtil.parse("2022-07-31 00:00:00");

        Date startDate = LocalDateTimeUtil.toDate(start);
        Date endDate = LocalDateTimeUtil.toDate(end);
        Long days = getIntervalDays(startDate, endDate);
//        Long hours = getIntervalHours(startDate, endDate);
//
//        double days = Double.valueOf(hours) / 24D;
//        days++;

        System.out.println(days);
    }

    /**
     * 获取间隔天数
     *
     * @param start
     * @param end
     * @return
     */
    public static Long getIntervalDays(Date start, Date end) {
        try {
            // 这样得到的差值是微秒级别
            long diff = end.getTime() - start.getTime();
            // 间隔天数
            long days = diff / (1000 * 60 * 60 * 24);
//            if (diff % (1000 * 60 * 60 * 24) > 0) {
//                days++;
//            }
            return days;
        } catch (Exception e) {
            return 0L;
        }
    }

    public static Long getIntervalHours(Date start, Date end) {
        try {
            // 这样得到的差值是微秒级别
            long diff = end.getTime() - start.getTime();
            // 间隔天数
            long hours = diff / (1000 * 60 * 60);
            return hours;
        } catch (Exception e) {
            return 0L;
        }
    }


}
