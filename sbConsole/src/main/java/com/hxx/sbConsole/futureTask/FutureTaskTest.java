package com.hxx.sbConsole.futureTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/*
cancel boolean （boolean mayInterruptIfRunning）
用来取消任务，如果取消任务成功则返回true，如果取消任务失败则返回false。
也就是说Future提供了三种功能：判断任务是否完成，能够中断任务，能够获取任务执行结果

isCancelled boolean 无
方法表示任务是否被取消成功，如果在任务正常完成前被取消成功，则返回 true。

isDone boolean 无
方法表示任务是否已经完成，若任务完成，则返回true；

get V 无
方法用来获取执行结果，这个方法会产生阻塞，会一直等到任务执行完毕才返回

get V （long timeout, TimeUnit unit）
用来获取执行结果，如果在指定时间内，还没获取到结果，就直接返回null

*/

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-22 11:53:20
 **/
public class FutureTaskTest {
    public static void main(String[] args) {
        Task task = new Task();
        FutureTask<Integer> integerFutureTask = new FutureTask<>(task);
        new Thread(integerFutureTask).start();

        try {
            System.out.println("task运行结果：" + integerFutureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // get
    static void case0() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        Future<Integer> future = service.submit(new CallableTask());
        try {
            // 获取返回值
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }

    // isDone
    static void case1() {
        ExecutorService service = Executors.newFixedThreadPool(20);
        Future<Integer> future = service.submit(new CallableErrTask());

        try {

            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                if (future.isDone()) {
                    break;
                }
                System.out.println(i);
                Thread.sleep(500);
            }

            // isDone 是否执行完毕
            // 正常终止、抛出异常、用户取消也为true
            System.out.println(future.isDone());
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // get超时
    static void case2() {
        //创建线程池
        ExecutorService service = Executors.newFixedThreadPool(10);
        //提交任务，并用 Future 接收返回结果
        List<Future> allFutures = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Future<String> future;
            if (i == 0 || i == 1) {
                future = service.submit(new SlowTask());
            } else {
                future = service.submit(new FastTask());
            }
            allFutures.add(future);
        }

        for (int i = 0; i < 4; i++) {
            Future<String> future = allFutures.get(i);
            try {
                //String result = future.get();

                // 带超时时间的get
                String result = future.get(5, TimeUnit.SECONDS);
                System.out.println(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                // 超时异常
                e.printStackTrace();
            }
        }
        service.shutdown();
    }

    static class CallableTask implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            Thread.sleep(3000);
            return new Random().nextInt();
        }
    }

    static class CallableErrTask implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            throw new IllegalArgumentException("Callable抛出异常");
        }
    }

    static class SlowTask implements Callable<String> {

        @Override
        public String call() throws Exception {
            Thread.sleep(5000);
            return "速度慢的任务";
        }
    }

    static class FastTask implements Callable<String> {

        @Override
        public String call() throws Exception {
            return "速度快的任务";
        }
    }

}
