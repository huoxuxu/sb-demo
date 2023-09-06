package javat.thread.completableFuture;

import com.hxx.sbConsole.SbConsoleApplication;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class CompletableFutureTest {

    // 异步执行任务，并返回结果
    @Test
    public void supplyAsync() throws ExecutionException, InterruptedException {
        System.out.println("==============test==============");
        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> "HELLO");
        String s = result.get();
        System.out.println(s);
    }

    // 异步执行任务，不返回结果
    @Test
    public void runAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> System.out.println("HELLO WORLD !"));
        voidCompletableFuture.get();
    }

    // 异步任务执行完时，继续执行 handleAsync
    @Test
    public void handleAsync() throws ExecutionException, InterruptedException {
        {
            System.out.println("==============test==============");
            CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> "HELLO");
            result.handleAsync((a, e) -> {
                if (e == null) return a + " WORLD!";
                else return "出现异常！";
            });
            String s = result.get();
            System.out.println(s);
        }
        {
            System.out.println("==============test==============");
            CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
                Integer a = null;
                return 1 / a + "1";
            });
            result.handleAsync((a, e) -> {
                if (e == null) return a + " WORLD!";
                else return "出现异常！" + ExceptionUtils.getStackTrace(e);
            });
            String s = result.get();
            System.out.println(s);
        }
    }

    @Test
    public void allOf() throws ExecutionException, InterruptedException {
        {
            CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "hello");
            CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "world");
            CompletableFuture<String> f3 = CompletableFuture.supplyAsync(
                    () -> {
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "!";
                    });

            // 使用allOf方法 f1 f2 f3 都执行结束之前一直阻塞
            CompletableFuture.allOf(f1, f2, f3)
                    .join();
            System.out.println("f1==>" + f1.get());
            System.out.println("f2==>" + f2.get());
            System.out.println("f3==>" + f3.get());
        }

        {
            CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "hello");
            CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "world");
            CompletableFuture<String> f3 = CompletableFuture.supplyAsync(
                    () -> {
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "!";
                    });

            List<String> r =
                    Stream.of(f1, f2, f3).map(d -> d.join()).collect(Collectors.toList());

            System.out.println(r);
        }
    }


}
