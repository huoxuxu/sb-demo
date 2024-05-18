package springmodule.async;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbConsole.springmodule.async.AsyncDemoService;
import com.hxx.sbConsole.springmodule.async.AsyncTaskDemoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class AsyncTaskDemoTest {

    @Autowired
    private AsyncTaskDemoService asyncTaskDemoService;
    @Autowired
    private AsyncDemoService asyncDemoService;

    @Test
    public void test() {
        System.out.println("==============test==============");

    }

    @Test
    public void test_execNoReturnTask() throws Exception {
        asyncTaskDemoService.execNoReturnTask();
        log.info("主线程执行finished");
        // 睡眠是为了等待异步方法执行完毕
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void test_execHaveReturnTask() throws Exception {
        Future<String> f = asyncTaskDemoService.execHaveReturnTask();
        log.info("主线程执行finished");
        log.info(f.get());
    }

    @Test
    public void doSomething() {
        System.out.println("==============test==============");
        asyncDemoService.doSomething("123");
        log.info("主线程执行finished");
    }

    @Test
    public void doSomething1() throws InterruptedException, ExecutionException {
        System.out.println("==============test==============");
        CompletableFuture<String> f = asyncDemoService.doSomething1("321");
        log.info("主线程执行finished");
        log.info("test-result:{}", f.get());
    }

}
