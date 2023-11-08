package javat.stream;

import com.hxx.sbConsole.SbConsoleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-30 11:18:45
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class ParallelStreamTest {

    @Test
    public void demo1() throws ExecutionException, InterruptedException {
        List<String> list = Arrays.asList("java", "python", "c++", "php", "go");

        ForkJoinPool customThreadPool = new ForkJoinPool(4);
        ForkJoinTask<?> submit1 = customThreadPool.submit(() -> list.parallelStream().forEach(d -> {
            System.out.println("任务1 Thread " + Thread.currentThread().getName() + " " + d);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        submit1.get();
    }

    // 自定义线程池
    @Test
    public void customerThread() throws ExecutionException, InterruptedException {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(i);
        }

        ForkJoinPool customThreadPool = new ForkJoinPool(3);
        // 带返回值
        {
            long actualTotal = customThreadPool
                    .submit(() -> data.parallelStream()
                            .reduce(0, Integer::sum))
                    .get();
            System.out.println(actualTotal);
        }
        // 不带返回值
        {
            System.out.println(LocalDateTime.now() + " 测试不带返回值");
            ForkJoinTask<?> joinTask = customThreadPool
                    .submit(() -> data.parallelStream()
                            .filter(d -> d > 0)
                            .forEach(d -> procCase1(d)));
            System.out.println(LocalDateTime.now() + " 测试不带返回值1");
            Object o = joinTask.get();
            System.out.println(o);
            System.out.println(LocalDateTime.now() + " 测试不带返回值2");
        }
        customThreadPool.shutdown();
    }

    static void procCase1(Integer d) {
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
