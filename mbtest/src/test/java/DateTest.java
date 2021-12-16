import com.hxx.mbtest.MbtestApplication;
import com.hxx.sbcommon.common.LocalDateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MbtestApplication.class)
public class DateTest {

    @Test
    public void of() {
        Date now = new Date();
        Long requestTime = now.getTime();//1639556697045  UTC时间
    }



}
