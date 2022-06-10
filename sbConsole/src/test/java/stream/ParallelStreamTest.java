package stream;

import com.hxx.sbConsole.SbConsoleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-30 11:18:45
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class ParallelStreamTest {
    // 自定义线程池
    @Test
    public void customerThread() throws ExecutionException, InterruptedException {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(i);
        }

        ForkJoinPool customThreadPool = new ForkJoinPool(3);
        // 带返回值
        long actualTotal = customThreadPool
                .submit(() -> data.parallelStream().reduce(0, Integer::sum)).get();
        System.out.println(actualTotal);

        // 不带返回值
        System.out.println(LocalDateTime.now() + " 测试不带返回值");
        ForkJoinTask<?> joinTask = customThreadPool
                .submit(() -> data
                        .parallelStream()
                        .filter(d -> d > 0)
                        .forEach(d -> procCase1(d)));
        System.out.println(LocalDateTime.now() + " 测试不带返回值1");
        Object o = joinTask.get();
        System.out.println(o);
        System.out.println(LocalDateTime.now() + " 测试不带返回值2");
    }

    static void procCase1(Integer d){
        if (d % 2 == 0) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(LocalDateTime.now() + " " + d);
    }
}
