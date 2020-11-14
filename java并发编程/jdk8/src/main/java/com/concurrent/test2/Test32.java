package com.concurrent.test2;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 查看源码
 */
public class Test32 {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Thread thread = new Thread(() -> {
            lock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "测试线程");
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        
        lock.lock();
        try {
            thread.interrupt();
            condition.signal();
        }
        finally {
            lock.unlock();
        }
        
     
    }
}

