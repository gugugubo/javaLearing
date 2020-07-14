package com.concurrent.test;

import java.util.concurrent.locks.LockSupport;

/**
 * wait 和 notify的缺点
 * 首先，需要保证先 wait 再 notify，否则 wait 线程永远得不到唤醒。因此使用了『运行标记』来判断该不该
 * wait
 * 第二，如果有些干扰线程错误地 notify 了 wait 线程，条件不满足时还要重新等待，使用了 while 循环来解决
 * 此问题
 * 最后，唤醒对象上的 wait 线程需要使用 notifyAll，因为『同步对象』上的等待线程可能不止一个
 * 
 * park 和 unpark 方法比较灵活，他俩谁先调用，谁后调用无所谓。并且是以线程为单位进行『暂停』和『恢复』，
 * 不需要『同步对象』和『运行标记』,不存在一个多个线程同时等待一个对象锁的现象，因为每个线程调用park的结果是等待它自己线程的upark来解锁
 */
public class Test36 {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) { }
            // 当没有『许可』时，当前线程暂停运行；有『许可』时，用掉这个『许可』，当前线程恢复运行
            LockSupport.park();
            System.out.println("1");
        });
        Thread t2 = new Thread(() -> {
            System.out.println("2");
            // 给线程 t1 发放『许可』（多次连续调用 unpark 只会发放一个『许可』）
            LockSupport.unpark(t1);
        });
        t1.start();
        t2.start();
    }
}
