package com.hxx.sbConsole.thread.completablefuture;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/*
CompletableFuture

创建：
# 无返回值
CompletableFuture.runAsync
# 有返回值
CompletableFuture.supplyAsync

任务执行完成后操作
# 将前一个CompletableFuture的结果作为输入，然后返回一个新的值。新的值会被包装在一个新的CompletableFuture中返回
thenApply
# 将多个异步操作串联起来，后一个异步操作是基于前一个异步操作的结果动态创建的
thenCompose

执行
# 等待所有的CompletableFuture都完成
CompletableFuture.allOf
# 等待任一CompletableFuture完成
CompletableFuture.anyOf

获取结果
# 获取异步任务的结果，会抛出：InterruptedException和ExecutionException
get()
# 获取异步计算的结果，不会抛出InterruptedException和ExecutionException异常，但会抛出其他异常
join()


*/
public class CompletableFutureDemo {

    public static void main(String[] args) {
        try {


        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }

    // 用法示例
    private static List<String> case0() {
        List<String> ls = new ArrayList<>();
        ls.add("1");
        ls.add("2");

        List<CompletableFuture<String>> futures = ls.stream()
                .map(param -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return "1";
                    } catch (Exception e) {
                        // 处理异常，返回默认值或进行其他错误处理操作
                        return null;
                    }
                }).exceptionally(ex -> null))
                .collect(Collectors.toList());
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .join();
    }

    // 创建CompletableFuture 示例
    private static void createAndRun() {
        /*
            当CompletableFuture完成计算后，执行 带返回值的Function
            thenApply(result -> result * 2);

            当CompletableFuture完成计算后，执行 不带返回值的Consumer
            thenAccept(result -> System.out.println("结果是: " + result));

            用于将两个CompletableFuture进行组合，
            第一个CompletableFuture的结果 会作为第二个CompletableFuture的参数来创建新的CompletableFuture
            thenCompose(preFutureResult -> CompletableFuture.supplyAsync(() -> preFutureResult * 2));
        */
        // 用于执行没有返回值的异步任务
        CompletableFuture<Void> voidFuture = CompletableFuture.runAsync(() -> {
            // 这里是异步执行的代码块，无返回值
            System.out.println("异步任务执行中...");
        });

        // 用于执行有返回值的异步任务
        CompletableFuture<Integer> supplyFuture = CompletableFuture.supplyAsync(() -> {
            // 这里是异步执行的代码块，有返回值
            return 42;
        });

        // 当CompletableFuture完成计算后，执行 带返回值的Function
        CompletableFuture<Integer> thenApplyFuture = supplyFuture
                .thenApply(result -> result * 2);

        // 当CompletableFuture完成计算后，执行 不带返回值的Consumer
        thenApplyFuture
                .thenAccept(result -> System.out.println("结果是: " + result));

        // 用于将两个CompletableFuture进行组合，
        // 第一个CompletableFuture的结果 会作为第二个CompletableFuture的参数来创建新的CompletableFuture
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> future2 = future1
                .thenCompose(preFutureResult -> CompletableFuture.supplyAsync(() -> preFutureResult * 2));
    }

    private static void procFuture() throws Exception {
        /*
            等待所有的CompletableFuture都完成
            CompletableFuture.allOf(voidFuture, voidFuture2);
            只要有一个CompletableFuture完成就会返回
            CompletableFuture.anyOf(future1, future2);

        */
        // 用于执行没有返回值的异步任务
        CompletableFuture<Void> voidFuture = CompletableFuture.runAsync(() -> {
            // 这里是异步执行的代码块，无返回值
            System.out.println("异步任务执行中...");
        });
        CompletableFuture<Void> voidFuture2 = CompletableFuture.runAsync(() -> {
            // 这里是异步执行的代码块，无返回值
            System.out.println("异步任务执行中2...");
        });

        // 等待所有的CompletableFuture都完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(voidFuture, voidFuture2);
        allFutures.get();
        System.out.println("所有任务完成");


        // 用于执行有返回值的异步任务
        CompletableFuture<Integer> supplyFuture = CompletableFuture.supplyAsync(() -> {
            // 这里是异步执行的代码块，有返回值
            return 42;
        });
        CompletableFuture<Integer> supplyFuture2 = CompletableFuture.supplyAsync(() -> {
            // 这里是异步执行的代码块，有返回值
            return 3;
        });

        // 等待所有的CompletableFuture都完成
        CompletableFuture<Void> allFutures2 = CompletableFuture.allOf(supplyFuture, supplyFuture2);
        allFutures2.get();
        System.out.println("所有任务完成2");


    }

    // 获取结果
    private static void futureResult() {
        /*
            获取异步任务的结果
            get方法
             会抛出：InterruptedException和ExecutionException

            join方法
             获取异步计算的结果，不会抛出InterruptedException和ExecutionException异常，但会抛出其他异常
        */

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                // 模拟一个耗时操作，比如网络请求或者数据库查询
                Thread.sleep(2000);
                return "Hello, World";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // 使用join方法获取结果，会阻塞当前线程直到future完成
        String result = future.join();
        System.out.println(result);
    }

    // 处理异常情况
    private static void procException() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                    if (LocalDateTime.now() != null)
                        throw new RuntimeException("发生错误");

                    return 1999;
                })
                .exceptionally(ex -> {
                    System.out.println("捕获到异常: " + ex.getMessage());
                    return 0;
                });

        System.out.println(future.get());
    }

    // 可同时处理正常和异常情况
    private static void procHandle() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                    //throw new RuntimeException("发生错误");
                    return 10;
                })
                .handle((result, ex) -> {
                    if (ex != null) {
                        System.out.println("捕获到异常: " + ex.getMessage());
                        return 0;
                    } else {
                        return result * 2;
                    }
                });

        System.out.println(future.get());
    }

}
