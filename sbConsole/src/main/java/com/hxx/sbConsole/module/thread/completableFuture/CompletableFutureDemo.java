package com.hxx.sbConsole.module.thread.completableFuture;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-11 13:43:48
 **/
@Slf4j
public class CompletableFutureDemo {

    public static void main(String[] args) {
        try {
            demo();
        } catch (Exception ex) {
            System.out.println(ex + "");
        }
        System.out.println("ok!");
    }

    public static void demo() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        //调用用户服务获取用户基本信息
        CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() ->
                //模拟查询商品耗时500毫秒
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "用户A";
        });

        //调用商品服务获取商品基本信息
        CompletableFuture<String> goodsFuture = CompletableFuture.supplyAsync(() ->
                //模拟查询商品耗时500毫秒
        {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "商品A";
        });

        System.out.println("获取用户信息:" + userFuture.get());
        System.out.println("获取商品信息:" + goodsFuture.get());

        //模拟主程序耗时时间
//        Thread.sleep(600);
        System.out.println("总共用时" + (System.currentTimeMillis() - startTime) + "ms");
    }

    // 做完第一个任务后，在做第二个任务，第二个任务也没有返回值
    public void thenRunAsync() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        CompletableFuture<Void> cp1 = CompletableFuture.runAsync(() -> {
            try {
                //执行任务A
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        CompletableFuture<Void> cp2 = cp1.thenRun(() -> {
            try {
                //执行任务B
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // get方法测试
        System.out.println(cp2.get());

        //模拟主程序耗时时间
        Thread.sleep(600);
        System.out.println("总共用时" + (System.currentTimeMillis() - startTime) + "ms");
    }

    // 第一个任务执行完成后，执行第二个任务，会将该任务的执行结果，作为入参传递给第二个任务，第二个任务没有返回值
    public void thenAccept() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        CompletableFuture<String> cp1 = CompletableFuture.supplyAsync(() -> {
            return "dev";

        });
        CompletableFuture<Void> cp2 = cp1.thenAccept((a) -> {
            System.out.println("上一个任务的返回结果为: " + a);
        });

        cp2.get();
    }

    // 第一个任务执行完后，执行第二个任务，会将该任务的执行结果，作为入参，传递给第二个任务，第二个任务有返回值
    public void thenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cp1 = CompletableFuture.supplyAsync(() -> {
                    return "dev";

                })
                .thenApply((a) -> {
                    if (Objects.equals(a, "dev")) {
                        return "dev";
                    }
                    return "prod";
                });

        System.out.println("当前环境为:" + cp1.get());

        //输出: 当前环境为:dev
    }

    public void whenComplete() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(() -> {

                    if (Math.random() < 0.5) {
                        throw new RuntimeException("出错了");
                    }
                    System.out.println("正常结束");
                    return 0.11;

                })
                .whenComplete((aDouble, throwable) -> {
                    if (aDouble == null) {
                        System.out.println("whenComplete aDouble is null");
                    } else {
                        System.out.println("whenComplete aDouble is " + aDouble);
                    }
                    if (throwable == null) {
                        System.out.println("whenComplete throwable is null");
                    } else {
                        System.out.println("whenComplete throwable is " + throwable.getMessage());
                    }
                });
        System.out.println("最终返回的结果 = " + future.get());
    }

    public void whenCompleteExceptionally() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(() -> {
                    if (Math.random() < 0.5) {
                        throw new RuntimeException("出错了");
                    }
                    System.out.println("正常结束");
                    return 0.11;

                })
                .whenComplete((aDouble, throwable) -> {
                    if (aDouble == null) {
                        System.out.println("whenComplete aDouble is null");
                    } else {
                        System.out.println("whenComplete aDouble is " + aDouble);
                    }
                    if (throwable == null) {
                        System.out.println("whenComplete throwable is null");
                    } else {
                        System.out.println("whenComplete throwable is " + throwable.getMessage());
                    }
                })
                .exceptionally((throwable) -> {
                    System.out.println("exceptionally中异常：" + throwable.getMessage());
                    return 0.0;
                });

        System.out.println("最终返回的结果 = " + future.get());
    }

    // runAfterBoth 不会把执行结果当做方法入参，且没有返回值
    // thenAcceptBoth 会将两个任务的执行结果作为方法入参，传递到指定方法中，且无返回值
    // thenCombine 会将两个任务的执行结果作为方法入参，传递到指定方法中，且有返回值 把两个任务组合在一起，当两个任务都执行结束以后触发事件回调
    // thenCompose 把两个任务组合在一起，这两个任务串行执行
    // thenAccept 第一个任务执行结束后触发第二个任务，且第一个任务的执行结果作为第二个任务的参数，这个方法纯粹接收上一个任务的结果，不返回新的计算值
    // thenApply 和 thenAccept 一样，但是它有返回值
    // thenRun 第一个任务执行完成后触发执行一个实现了Runnable 接口的任务
    public void testCompletableThenCombine() throws ExecutionException, InterruptedException {
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //开启异步任务1
        CompletableFuture<Integer> task = CompletableFuture.supplyAsync(() -> {
            System.out.println("异步任务1，当前线程是：" + Thread.currentThread()
                    .getId());
            int result = 1 + 1;
            System.out.println("异步任务1结束");
            return result;
        }, executorService);

        //开启异步任务2
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("异步任务2，当前线程是：" + Thread.currentThread()
                    .getId());
            int result = 1 + 1;
            System.out.println("异步任务2结束");
            return result;
        }, executorService);

        //任务组合
        CompletableFuture<Integer> task3 = task.thenCombineAsync(task2, (f1, f2) -> {
            System.out.println("执行任务3，当前线程是：" + Thread.currentThread()
                    .getId());
            System.out.println("任务1返回值：" + f1);
            System.out.println("任务2返回值：" + f2);
            return f1 + f2;
        }, executorService);

        Integer res = task3.get();
        System.out.println("最终结果：" + res);
    }

    // runAfterEither 不会把执行结果当作方法入参，且没有返回值
    // acceptEither 会将已经执行完成的任务，作为方法入参，传递到指定方法中，且无返回值
    // applyToEither 会将已经执行完成的任务，作为方法入参，传递到指定方法中，且有返回值
    public void testCompletableEitherAsync() {
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //开启异步任务1
        CompletableFuture<Integer> task = CompletableFuture.supplyAsync(() -> {
            System.out.println("异步任务1，当前线程是：" + Thread.currentThread()
                    .getId());

            int result = 1 + 1;
            System.out.println("异步任务1结束");
            return result;
        }, executorService);

        //开启异步任务2
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("异步任务2，当前线程是：" + Thread.currentThread()
                    .getId());
            int result = 1 + 2;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("异步任务2结束");
            return result;
        }, executorService);

        //任务组合
        task.acceptEitherAsync(task2, (res) -> {
            System.out.println("执行任务3，当前线程是：" + Thread.currentThread()
                    .getId());
            System.out.println("上一个任务的结果为：" + res);
        }, executorService);
    }

    // CompletableFuture.allOf 等待任务全部执行完成
    public void testCompletableAallOf() throws ExecutionException, InterruptedException {
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //开启异步任务1
        CompletableFuture<Integer> task = CompletableFuture.supplyAsync(() -> {
            System.out.println("异步任务1，当前线程是：" + Thread.currentThread()
                    .getId());
            int result = 1 + 1;
            System.out.println("异步任务1结束");
            return result;
        }, executorService);

        //开启异步任务2
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("异步任务2，当前线程是：" + Thread.currentThread()
                    .getId());
            int result = 1 + 2;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("异步任务2结束");
            return result;
        }, executorService);

        //开启异步任务3
        CompletableFuture<Integer> task3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("异步任务3，当前线程是：" + Thread.currentThread()
                    .getId());
            int result = 1 + 3;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("异步任务3结束");
            return result;
        }, executorService);

        //任务组合
        CompletableFuture<Void> allOf = CompletableFuture.allOf(task, task2, task3);

        //等待所有任务完成
        allOf.get();
        //获取任务的返回结果
        System.out.println("task结果为：" + task.get());
        System.out.println("task2结果为：" + task2.get());
        System.out.println("task3结果为：" + task3.get());
    }

    // CompletableFuture.anyOf 等待任务有一个执行完成即可
    public void testCompletableAnyOf() throws ExecutionException, InterruptedException {
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //开启异步任务1
        CompletableFuture<Integer> task = CompletableFuture.supplyAsync(() -> {
            int result = 1 + 1;
            return result;
        }, executorService);

        //开启异步任务2
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            int result = 1 + 2;
            return result;
        }, executorService);

        //开启异步任务3
        CompletableFuture<Integer> task3 = CompletableFuture.supplyAsync(() -> {
            int result = 1 + 3;
            return result;
        }, executorService);

        //任务组合
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(task, task2, task3);
        //只要有一个有任务完成
        Object o = anyOf.get();
        System.out.println("完成的任务的结果：" + o);
    }

}
