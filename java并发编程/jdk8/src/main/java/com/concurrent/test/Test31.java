package com.concurrent.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
@Slf4j
/**
 * 使用lockInterruptibly获取到锁，这样的在获取等待锁的时候是可以被打断的
 */
public class Test31 {

    public static void main(String[] args) {

        Lock lock = new ReentrantLock();
        
        Thread thread = new Thread(() -> {
            try {
                /**
                 * 注意这里使用的是lockInterruptibly方法，如果使用lock.lock()方法，那么这里
                 * 等待的时候是不可以被打断的
                 */
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.info("被打断啦");
                // 这里如果出了错不要再往下执行了
                return;
            }
            
            
            try{
                log.info("执行完啦，获取到了锁，没被打断");
            }finally {
                lock.unlock();
            }
        }, "thread-1");
        
        lock.lock();
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // 打断线程thread，原本它的状态是在等待锁的，我们在它等待锁的时候打断了，不让它继续等待了
        thread.interrupt();

        log.info("主线程执行结束");
    }
    
}
