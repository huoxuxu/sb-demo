package com.hxx.sbConsole.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-14 10:53:25
 **/
public class LockDemo {
    private Lock lock = new ReentrantLock();

    //需要参与同步的方法
    private void lock(Thread thread) {
        try {
            lock.lock();
            System.out.println("线程名" + thread.getName() + "获得了锁");
        } catch (Exception e) {
            System.out.println(e + "");
        } finally {
            System.out.println("线程名" + thread.getName() + "释放了锁");
            lock.unlock();
        }
    }

    private void tryLock(Thread thread) {
        if (lock.tryLock()) {
            try {
                System.out.println("线程名" + thread.getName() + "获得了锁");
            } catch (Exception e) {
                System.out.println(e + "");
            } finally {
                System.out.println("线程名" + thread.getName() + "释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println("我是" + Thread.currentThread()
                    .getName() + "有人占着锁，我就不要啦");
        }
    }

    public static void main(String[] args) {

    }

    static void lock(){
        LockDemo lockTest = new LockDemo();

        //线程1
        Thread t1 = new Thread(() -> lockTest.lock(Thread.currentThread()), "t1");
        //线程2
        Thread t2 = new Thread(() -> lockTest.lock(Thread.currentThread()), "t2");

        t1.start();
        t2.start();

//执行情况：线程名t1获得了锁
//         线程名t1释放了锁
//         线程名t2获得了锁
//         线程名t2释放了锁
    }

    static void tryLock(){
        LockDemo lockTest = new LockDemo();

        //线程1
        Thread t1 = new Thread(() -> lockTest.tryLock(Thread.currentThread()), "t1");
        //线程2
        Thread t2 = new Thread(() -> lockTest.tryLock(Thread.currentThread()), "t2");

        t1.start();
        t2.start();

//执行结果： 线程名t2获得了锁
//         我是t1有人占着锁，我就不要啦
//         线程名t2释放了锁
    }



}
