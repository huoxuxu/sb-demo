package javat.basic.numericVal;

import com.hxx.sbConsole.SbConsoleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class LongTest {

    @Test
    public void test() {
        System.out.println("==============test==============");

    }

    @Test
    public void test_overflow() {
        System.out.println("==============test==============");
        {
            long a1 = Long.MAX_VALUE;
            System.out.println(a1);// 9223372036854775807
            System.out.println(a1 + 1);// -9223372036854775808
            System.out.println(a1 + 999);// -9223372036854774810
        }
    }

}
