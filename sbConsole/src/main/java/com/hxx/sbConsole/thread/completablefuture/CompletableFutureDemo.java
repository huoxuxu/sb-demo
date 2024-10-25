package com.hxx.sbConsole.thread.completablefuture;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {

    public static void main(String[] args) {
        try {


        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
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
