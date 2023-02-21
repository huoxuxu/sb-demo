package javat;

import com.hxx.sbConsole.SbConsoleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class StopWatchTest {

    @Test
    public void test() {
        System.out.println("==============test==============");

    }

    @Test
    public void test2() throws InterruptedException {
        System.out.println("==============test==============");
        StopWatch stopWatch = new StopWatch("func2");
        stopWatch.start("phase1");
        System.out.println("phase1 do something....");
        Thread.sleep(1000);
        stopWatch.stop();
        System.out.printf("phase1 cost time %d ms\n", stopWatch.getLastTaskTimeMillis());
        stopWatch.start("phase2");
        System.out.println("phase2 do something....");
        Thread.sleep(2000);
        stopWatch.stop();
        System.out.printf("phase2 cost time %d ms\n", stopWatch.getLastTaskTimeMillis());
        stopWatch.start("phase3");
        System.out.println("phase3 do something....");
        Thread.sleep(3000);
        stopWatch.stop();
        System.out.printf("phase3 cost time %d ms\n", stopWatch.getLastTaskTimeMillis());
        stopWatch.start("phase4");
        System.out.println("phase4 do something....");
        Thread.sleep(4000);
        stopWatch.stop();
        System.out.printf("phase4 cost time %d ms\n", stopWatch.getLastTaskTimeMillis());
        System.out.printf("func1 cost %d ms\n", stopWatch.getTotalTimeMillis());
        System.out.println("stopWatch.prettyPrint() = " + stopWatch.prettyPrint());
    }

}
