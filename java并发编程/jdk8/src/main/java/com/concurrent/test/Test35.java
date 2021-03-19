package com.concurrent.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test35 {
    // 用来同步的对象
    static Object obj = new Object();
    // t2 运行标记， 代表 t2 是否执行过
    static boolean t2runed = false;
    static boolean t2runed2 = false;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (obj) {
                // 如果 t2 没有执行过，标记t2runed为false
                while (!t2runed) {
                    try {
                        // t1 先等一会
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }
            }
            System.out.println(1);
        });
        
        Thread t2 = new Thread(() -> {
            System.out.println(2);
            synchronized (obj) {
                // 修改运行标记，使线程t1可以运行
                t2runed = true;
                // 通知 obj 上等待的线程（可能有多个，因此需要用 notifyAll）
                obj.notifyAll();
            }
        });
        t1.start();
        t2.start();
        test();
    }



    public static void test() {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Thread t1 = new Thread(() -> {
            try{
                lock.lock();
                // 如果 t2 没有执行过，标记t2runed为false
                while (!t2runed2) {
                    try {
                        // t1 先等一会
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }finally {
                lock.unlock();
            }
            System.out.println(111111);
        });

        Thread t2 = new Thread(() -> {
            System.out.println(22222);
            try {
                lock.lock();
                // 修改运行标记，使线程t1可以运行
                t2runed2 = true;
                // 通知 obj 上等待的线程（可能有多个，因此需要用 notifyAll）
                condition.signal();
            }finally {
                lock.unlock();
            }
        });
        t1.start();
        t2.start();
    }
}
