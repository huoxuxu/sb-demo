package com.hxx.sbConsole.thread.threadLocal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-06 9:30:53
 **/
public class NotThreadSafe {
    private final ThreadLocal<Integer> count = ThreadLocal.withInitial(() -> 0);

    public void increment() {
        Integer countValue = count.get();
        countValue++;
        count.set(countValue);
    }

    public void decrement() {
        Integer countValue = count.get();
        countValue--;
        count.set(countValue);
    }

    public int getValue() {
        return count.get();
    }

    public void remove() {
        count.remove();
    }

    public static void main(String[] args) {
        NotThreadSafe notThreadSafe = new NotThreadSafe();
        {
            new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(1000);
                        notThreadSafe.increment();
                        System.out.println("A：increment i=" + notThreadSafe.getValue());
//                        notThreadSafe.decrement();
//                        System.out.println("A：decrement i=" + notThreadSafe.getValue());
                    }
                } catch (InterruptedException e) {
                } finally {
                    notThreadSafe.remove();
                }
            }).start();
        }
        {
            new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(500);
                        notThreadSafe.increment();
                        System.out.println("B：increment i=" + notThreadSafe.getValue());
//                        notThreadSafe.decrement();
//                        System.out.println("B：decrement i=" + notThreadSafe.getValue());
                    }
                } catch (InterruptedException e) {
                } finally {
                    notThreadSafe.remove();
                }
            }).start();
        }
        System.out.println("Z：decrement i=" + notThreadSafe.getValue());
    }
}
