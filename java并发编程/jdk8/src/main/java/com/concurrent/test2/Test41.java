package com.concurrent.test2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * @Author 古春波
 * @Description sem测试
 * @Date 2020/11/16 9:50
 * @Version 1.0
 **/
@Slf4j
public class Test41 {

    private static Semaphore sem = new Semaphore(1);

    @Slf4j(topic = "Thread1")
    private static class Thread1 extends Thread {
        @Override
        public void run() {
            sem.acquireUninterruptibly();
            log.info("Thread 1 取消获取锁 sem 之后");
        }
    }

    @Slf4j(topic = "Thread2")
    private static class Thread2 extends Thread {
        @Override
        public void run() {
            sem.release();
            log.info("Thread 2 取消获取锁");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Thread t1 = new Thread1();
            Thread t2 = new Thread1();
            Thread t3 = new Thread2();
            Thread t4 = new Thread2();
            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            System.out.println(i);
        }
    }
    
}
